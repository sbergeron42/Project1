let warehouses = [];
let inventory = [];

// Load data from server
async function loadWarehouses() {
  const res = await fetch('http://localhost:8282/warehouses');
  warehouses = await res.json();
  showWarehouses();
  showDashboard();
}

async function loadInventory() {
  const res = await fetch('http://localhost:8282/inventories');
  if (!res.ok) {
    throw new Error('Failed to load inventory');
  }
  inventory = await res.json();
  showInventory();
  console.log(inventory);
  showDashboard();
}

// Simple alert function
function showMessage(message) {
  alert(message);
}

// Show/hide sections
function showSection(sectionName) {
  // Hide all sections
  document.getElementById('dashboardSection').style.display = 'none';
  document.getElementById('warehousesSection').style.display = 'none';
  document.getElementById('inventorySection').style.display = 'none';
  document.getElementById('reportsSection').style.display = 'none';
  
  // Show the one we want
  if (sectionName === 'dashboard') {
    document.getElementById('dashboardSection').style.display = 'block';
    showDashboard();
  }
  if (sectionName === 'warehouses') {
    document.getElementById('warehousesSection').style.display = 'block';
    showWarehouses();
  }
  if (sectionName === 'inventory') {
    document.getElementById('inventorySection').style.display = 'block';
    showInventory();
  }
  if (sectionName === 'reports') {
    document.getElementById('reportsSection').style.display = 'block';
    showReports();
  }
}

// Show dashboard
function showDashboard() {
  const container = document.getElementById('dashboardSection');
  
  let totalWarehouses = warehouses.length;
  let totalItems = inventory.length;
  let totalQuantity = 0;
  
  for (let i = 0; i < inventory.length; i++) {
    totalQuantity = totalQuantity + inventory[i].quantity;
  }
  
  let html = '<h2>Dashboard</h2>';
  html += '<div class="row">';
  html += '<div class="col-md-4"><div class="card p-3">';
  html += '<h5>Warehouses</h5>';
  html += '<h3>' + totalWarehouses + '</h3>';
  html += '</div></div>';
  
  html += '<div class="col-md-4"><div class="card p-3">';
  html += '<h5>Inventory Items</h5>';
  html += '<h3>' + totalItems + '</h3>';
  html += '</div></div>';
  
  html += '<div class="col-md-4"><div class="card p-3">';
  html += '<h5>Total Quantity</h5>';
  html += '<h3>' + totalQuantity + '</h3>';
  html += '</div></div>';
  html += '</div>';
  
  container.innerHTML = html;
}

// Show warehouses list
function showWarehouses() {
  const container = document.getElementById('warehousesSection');
  
  let html = '<h2>Warehouses</h2>';
  html += '<button class="btn btn-primary mb-3" onclick="showWarehouseForm()">Add Warehouse</button>';
  
  if (warehouses.length === 0) {
    html += '<p>No warehouses yet</p>';
  } else {
    html += '<div class="row">';
    for (let i = 0; i < warehouses.length; i++) {
      let w = warehouses[i];
      html += '<div class="col-md-4 mb-3">';
      html += '<div class="card">';
      html += '<div class="card-body">';
      html += '<h5>' + w.name + '</h5>';
      html += '<p>' + w.location + '</p>';
      html += '<p>Capacity: ' + w.maxCapacity + '</p>';
      html += '<button class="btn btn-sm btn-primary" onclick="editWarehouse(' + w.id + ')">Edit</button> ';
      html += '<button class="btn btn-sm btn-danger" onclick="deleteWarehouse(' + w.id + ')">Delete</button>';
      html += '</div></div></div>';
    }
    html += '</div>';
  }
  
  container.innerHTML = html;
}

// Show add/edit warehouse form
function showWarehouseForm(warehouseId) {
  const formDiv = document.getElementById('warehouseFormDiv');
  formDiv.style.display = 'block';
  
  document.getElementById('warehouseName').value = '';
  document.getElementById('warehouseLocation').value = '';
  document.getElementById('warehouseCapacity').value = '';
  document.getElementById('editWarehouseId').value = '';
  
  if (warehouseId) {
    // Find the warehouse
    for (let i = 0; i < warehouses.length; i++) {
      if (warehouses[i].id === warehouseId) {
        document.getElementById('warehouseName').value = warehouses[i].name;
        document.getElementById('warehouseLocation').value = warehouses[i].location;
        document.getElementById('warehouseCapacity').value = warehouses[i].maxCapacity;
        document.getElementById('editWarehouseId').value = warehouses[i].id;
        break;
      }
    }
  }
}

function hideWarehouseForm() {
  document.getElementById('warehouseFormDiv').style.display = 'none';
}

function hideInventoryForm() {
  document.getElementById('inventoryFormDiv').style.display = 'none';
}

// Save warehouse
async function saveWarehouse() {
  const name = document.getElementById('warehouseName').value;
  const location = document.getElementById('warehouseLocation').value;
  const capacity = document.getElementById('warehouseCapacity').value;
  const editId = document.getElementById('editWarehouseId').value;
  
  if (!name || !location || !capacity) {
    showMessage('Please fill all fields');
    return;
  }
  
  if (editId) {
    // Edit existing warehouse on server
    const updatedWarehouse = {
      name: name,
      location: location,
      maxCapacity: parseInt(capacity)
    };
    
    const response = await fetch('http://localhost:8282/warehouses/' + editId, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(updatedWarehouse)
    });
    
    if (response.ok) {
      showMessage('Warehouse updated!');
      await loadWarehouses();
    } else {
      showMessage('Error updating warehouse');
    }
  } else {
    // Add new warehouse to server
    const newWarehouse = {
      name: name,
      location: location,
      maxCapacity: parseInt(capacity)
    };
    
    const response = await fetch('http://localhost:8282/warehouses', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newWarehouse)
    });
    
    if (response.ok) {
      showMessage('Warehouse added!');
      // Reload warehouses from server to get the new one
      await loadWarehouses();
    } else {
      showMessage('Error adding warehouse');
    }
  }
  
  hideWarehouseForm();
  showWarehouses();
  showDashboard();
}

function editWarehouse(id) {
  showWarehouseForm(id);
}

// Delete warehouse
async function deleteWarehouse(id) {
  // Check if any inventory in this warehouse
  for (let i = 0; i < inventory.length; i++) {
    if (inventory[i].warehouseId === id) {
      showMessage('Cannot delete warehouse with inventory!');
      return;
    }
  }
  
  const confirmDelete = confirm('Are you sure you want to delete this warehouse?');
  if (!confirmDelete) {
    return;
  }
  
  // Delete from server
  const response = await fetch('http://localhost:8282/warehouses/' + id, {
    method: 'DELETE'
  });
  
  if (response.ok) {
    showMessage('Warehouse deleted!');
    await loadWarehouses();
  } else {
    showMessage('Error deleting warehouse');
  }
  
  showWarehouses();
  showDashboard();
}

function showInventory() {
  const container = document.getElementById('inventorySection');

  let html = '<h2>Inventory</h2>';
  html += '<button class="btn btn-primary mb-3" onclick="showInventoryForm()">Add Item</button>';
  html += `
    <div class="card p-3 mb-3" style="display:inline-block; vertical-align:middle;">
      <h5>Transfer Inventory</h5>
      <div class="mb-2">
        <input type="number" id="transferItemIdInput" placeholder="Item ID" class="form-control" style="width:120px; display:inline-block;">
        <input type="number" id="transferQuantityInput" placeholder="Quantity" class="form-control" style="width:120px; display:inline-block; margin-left:5px;">
        <select id="transferTargetWarehouseInput" class="form-control" style="width:150px; display:inline-block; margin-left:5px;">
          <option value="">Select Warehouse</option>
        </select>
        <button class="btn btn-success" onclick="submitTransfer()" style="margin-left:5px;">Transfer</button>
      </div>
    </div>
  `;

  if (inventory.length === 0) {
    html += '<p>No inventory items yet</p>';
  } else {
    html += '<table class="table">';
    html += '<tr><th>SKU</th><th>Name</th><th>Warehouse</th><th>Storage Location</th><th>Quantity</th><th>Actions</th></tr>';

    for (let i = 0; i < inventory.length; i++) {
      let item = inventory[i];

      html += '<tr>';
      html += '<td>' + item.product.sku + '</td>';      // Nested product object
      html += '<td>' + item.product.name + '</td>';     // Nested product object
      html += '<td>' + item.warehouse.name + '</td>';   // Nested warehouse object
      html += '<td>' + item.storageLocation + '</td>';
      html += '<td>' + item.quantity + '</td>';
      html += '<td>';
      html += '<button class="btn btn-sm btn-primary" onclick="editInventory(' + item.id + ')">Edit</button> ';
      html += '<button class="btn btn-sm btn-danger" onclick="deleteInventory(' + item.id + ')">Delete</button>';
      html += '</td>';
      html += '</tr>';
    }

    html += '</table>';
  }

  container.innerHTML = html;
}

// Show inventory form
function showInventoryForm(itemId) {
  const formDiv = document.getElementById('inventoryFormDiv');
  formDiv.style.display = 'block';
  
  // Reset form
  document.getElementById('itemSKU').value = '';
  document.getElementById('itemName').value = '';
  document.getElementById('itemDescription').value = '';
  document.getElementById('itemQuantity').value = '';
  document.getElementById('editItemId').value = '';
  
  // Fill warehouse dropdown
  const warehouseSelect = document.getElementById('itemWarehouse');
  let options = '<option value="">Select Warehouse</option>';
  for (let i = 0; i < warehouses.length; i++) {
    options += '<option value="' + warehouses[i].id + '">' + warehouses[i].name + '</option>';
  }
  warehouseSelect.innerHTML = options;
  
  if (itemId) {
    // Find the item
    for (let i = 0; i < inventory.length; i++) {
      if (inventory[i].id === itemId) {
        const item = inventory[i];
        document.getElementById('itemSKU').value = item.product.sku;
        document.getElementById('itemName').value = item.product.name;
        document.getElementById('itemManufacturer').value = item.product.manufacturer;
        document.getElementById('itemDescription').value = item.product.description || '';
        document.getElementById('itemWarehouse').value = item.warehouse.id;
        document.getElementById('itemStorageLocation').value = item.storageLocation;
        document.getElementById('itemQuantity').value = item.quantity;
        document.getElementById('editItemId').value = item.id;
        break;
      }
    }
  }
}
async function saveInventory() {
  const sku = document.getElementById('itemSKU').value;
  const name = document.getElementById('itemName').value;
  const manufacturer = document.getElementById('itemManufacturer').value;
  const description = document.getElementById('itemDescription').value;
  const warehouseId = parseInt(document.getElementById('itemWarehouse').value);
  const storageLocation = document.getElementById('itemStorageLocation').value;
  const quantity = parseInt(document.getElementById('itemQuantity').value);
  const editId = document.getElementById('editItemId').value;

  if (!sku || !name || !manufacturer || !warehouseId || !quantity || !storageLocation) {
    showMessage('Please fill all required fields');
    return;
  }

  const payload = {
    quantity: quantity,
    storageLocation: storageLocation,
    product: {
      sku: sku,
      name: name,
      manufacturer: manufacturer,
      description: description
    }
  };

  if (editId) {
    // Editing existing item
    const response = await fetch(`http://localhost:8282/warehouses/${warehouseId}/inventories/${editId}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (response.ok) {
      showMessage('Item updated!');
      await loadInventory();
    } else {
      showMessage('Error updating item');
    }
  } else {
    // Adding new item
    const response = await fetch(`http://localhost:8282/warehouses/${warehouseId}/inventories`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (response.ok) {
      showMessage('Item added!');
      await loadInventory();
    } else {
      showMessage('Error adding item');
    }
  }

  hideInventoryForm();
  showInventory();
  showDashboard();
}


function editInventory(id) {
  showInventoryForm(id);
}

// Delete inventory
async function deleteInventory(id) {
  const confirmDelete = confirm('Are you sure you want to delete this item?');
  if (!confirmDelete) return;

  // Find the warehouse ID for this inventory item
  const item = inventory.find(i => i.id === id);
  if (!item) {
    showMessage('Item not found!');
    return;
  }

  const warehouseId = item.warehouse.id;

  try {
    const response = await fetch(`http://localhost:8282/warehouses/${warehouseId}/inventory?ids=${id}`, {
      method: 'DELETE'
    });

    if (response.ok) {
      showMessage('Item deleted!');
      await loadInventory(); // reload from server
    } else {
      showMessage('Error deleting item!');
    }
  } catch (err) {
    console.error(err);
    showMessage('Failed to delete item!');
  }
}

async function submitTransfer() {
  const inventoryId = parseInt(document.getElementById('transferItemId').value);
  const targetWarehouseId = parseInt(document.getElementById('transferTargetWarehouse').value);
  const quantity = parseInt(document.getElementById('transferQuantity').value);

  if (!inventoryId || !targetWarehouseId || !quantity || quantity <= 0) {
    showMessage('Please fill all fields with valid values.');
    return;
  }

  // Find the source warehouse from inventory
  const item = inventory.find(i => i.id === inventoryId);
  if (!item) {
    showMessage('Item not found!');
    return;
  }

  const body = {
    inventoryId: inventoryId,
    sourceWarehouseId: item.warehouse.id,
    targetWarehouseId: targetWarehouseId,
    quantity: quantity
  };

  try {
    const res = await fetch('http://localhost:8282/warehouses/transfer', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });

    if (res.ok) {
      showMessage('Transfer successful!');
      hideTransferForm();
      await loadInventory(); // Refresh inventory table
    } else {
      showMessage('Transfer failed!');
    }
  } catch (err) {
    console.error(err);
    showMessage('Error during transfer!');
  }
}


// Show reports
function showReports() {
  const container = document.getElementById('reportsSection');
  
  let html = '<h2>Reports</h2>';
  html += '<div class="card"><div class="card-body">';
  html += '<h5>Inventory by Warehouse</h5>';
  html += '<table class="table">';
  html += '<tr><th>Warehouse</th><th>Items</th><th>Total Quantity</th></tr>';
  
  for (let i = 0; i < warehouses.length; i++) {
    let w = warehouses[i];
    let itemCount = 0;
    let totalQty = 0;
    
    for (let j = 0; j < inventory.length; j++) {
      if (inventory[j].warehouseId === w.id) {
        itemCount++;
        totalQty += inventory[j].quantity;
      }
    }
    
    html += '<tr>';
    html += '<td>' + w.name + '</td>';
    html += '<td>' + itemCount + '</td>';
    html += '<td>' + totalQty + '</td>';
    html += '</tr>';
  }
  
  html += '</table>';
  html += '</div></div>';
  
  container.innerHTML = html;
}

// Setup navigation
function setupNavigation() {
  const navLinks = document.querySelectorAll('.nav-link[data-page]');
  
  for (let i = 0; i < navLinks.length; i++) {
    navLinks[i].onclick = function(e) {
      e.preventDefault();
      
      // Remove active from all
      for (let j = 0; j < navLinks.length; j++) {
        navLinks[j].classList.remove('active');
      }
      
      // Add active to clicked
      this.classList.add('active');
      
      // Show the section
      const page = this.getAttribute('data-page');
      showSection(page);
    };
  }
}

// Start the app
function init() {
  // Create the page sections
  const container = document.getElementById('pageContainer');
  container.innerHTML = `
    <div id="dashboardSection"></div>
    <div id="warehousesSection" style="display:none;"></div>
    <div id="inventorySection" style="display:none;"></div>
    <div id="reportsSection" style="display:none;"></div>
    
    <!-- Warehouse Form -->
    <div id="warehouseFormDiv" style="display:none;" class="card p-3 mb-3">
      <h4>Warehouse Form</h4>
      <input type="hidden" id="editWarehouseId">
      <div class="mb-2">
        <label>Name:</label>
        <input type="text" id="warehouseName" class="form-control">
      </div>
      <div class="mb-2">
        <label>Location:</label>
        <input type="text" id="warehouseLocation" class="form-control">
      </div>
      <div class="mb-2">
        <label>Capacity:</label>
        <input type="number" id="warehouseCapacity" class="form-control">
      </div>
      <button class="btn btn-success" onclick="saveWarehouse()">Save</button>
      <button class="btn btn-secondary" onclick="hideWarehouseForm()">Cancel</button>
    </div>
    
    <!-- Inventory Form -->
    <div id="inventoryFormDiv" style="display:none;" class="card p-3 mb-3">
      <h4>Inventory Form</h4>
      <input type="hidden" id="editItemId">
      
      <div class="mb-2">
        <label>SKU:</label>
        <input type="text" id="itemSKU" class="form-control">
      </div>
      
      <div class="mb-2">
        <label>Name:</label>
        <input type="text" id="itemName" class="form-control">
      </div>
      
      <div class="mb-2">
        <label>Manufacturer:</label>
        <input type="text" id="itemManufacturer" class="form-control">
      </div>

      <div class="mb-2">
        <label>Description:</label>
        <textarea id="itemDescription" class="form-control"></textarea>
      </div>
      
      <div class="mb-2">
        <label>Warehouse:</label>
        <select id="itemWarehouse" class="form-control"></select>
      </div>
      
      <div class="mb-2">
        <label>Storage Location:</label>
        <input type="text" id="itemStorageLocation" class="form-control">
      </div>
      
      <div class="mb-2">
        <label>Quantity:</label>
        <input type="number" id="itemQuantity" class="form-control">
      </div>
      
      <button class="btn btn-success" onclick="saveInventory()">Save</button>
      <button class="btn btn-secondary" onclick="hideInventoryForm()">Cancel</button>
    </div>

    <!-- Transfer Form -->
    <div id="transferFormDiv" style="display:none;" class="card p-3 mb-3">
      <h4>Transfer Inventory</h4>
      <input type="hidden" id="transferItemId">
      
      <div class="mb-2">
        <label>Quantity:</label>
        <input type="number" id="transferQuantity" class="form-control">
      </div>
      
      <div class="mb-2">
        <label>Target Warehouse:</label>
        <select id="transferTargetWarehouse" class="form-control"></select>
      </div>
      
      <button class="btn btn-success" onclick="submitTransfer()">Transfer</button>
      <button class="btn btn-secondary" onclick="hideTransferForm()">Cancel</button>
    </div>

  `;
  
  setupNavigation();
  loadWarehouses();
  loadInventory();
  showDashboard();
}

// Start everything when page loads
init();

window.showWarehouseForm = showWarehouseForm;
window.editWarehouse = editWarehouse;
window.deleteWarehouse = deleteWarehouse;
window.showInventoryForm = showInventoryForm;
window.editInventory = editInventory;
window.deleteInventory = deleteInventory;
window.saveWarehouse = saveWarehouse;
window.saveInventory = saveInventory;
window.hideWarehouseForm = hideWarehouseForm;
window.hideInventoryForm = hideInventoryForm;
window.showTransferForm = showTransferForm;
window.hideTransferForm = hideTransferForm;
window.submitTransfer = submitTransfer;
