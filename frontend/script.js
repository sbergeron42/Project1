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
  
  // Different tabs
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
  let totalWarehouses = warehouses.length;
  let totalItems = inventory.length;
  let totalQuantity = 0;
  
  for (let i = 0; i < inventory.length; i++) {
    totalQuantity = totalQuantity + inventory[i].quantity;
  }

  // Update stat cards
  document.getElementById('totalWarehouses').textContent = totalWarehouses;
  document.getElementById('totalItems').textContent = totalItems;
  document.getElementById('totalQuantity').textContent = totalQuantity;

  // Check for alerts
  let alerts = [];
  for (let i = 0; i < warehouses.length; i++) {
    let w = warehouses[i];
    if (w.maxCapacity > 0) {
      let percentage = (w.currentCapacity / w.maxCapacity) * 100;
      if (percentage >= 90) {
        alerts.push({
          warehouse: w.name,
          percentage: Math.round(percentage)
        });
      }
    }
  }
  
  // Update alerts container
  const alertsContainer = document.getElementById('alertsContainer');
  
  if (alerts.length === 0) {
    alertsContainer.innerHTML = '<p class="mb-0 text-muted">No recent alerts</p>';
  } else {
    let html = '<div class="alert alert-warning mb-0 d-flex justify-content-between align-items-center">';
    html += '<div>';
    for (let i = 0; i < alerts.length; i++) {
      html += '<strong>' + alerts[i].warehouse + '</strong> is at ' + alerts[i].percentage + '% capacity';
      if (i < alerts.length - 1) {
        html += '<br>';
      }
    }
    html += '</div>';
    html += '<button class="btn btn-primary btn-sm" data-action="goto-warehouses">Take Action</button>';
    html += '</div>';
    alertsContainer.innerHTML = html;
  }

  loadRecentActivity();
}

// Show warehouses list
function showWarehouses() {
  const container = document.getElementById('warehousesListContainer');
  
  if (warehouses.length === 0) {
    container.innerHTML = '<p>No warehouses yet</p>';
    return;
  }
  
  let html = '<div class="row">';
  for (let i = 0; i < warehouses.length; i++) {
    let w = warehouses[i];

    // calculate capacity percentage
    let percentage = 0;
    if (w.maxCapacity > 0) {
      percentage = (w.currentCapacity / w.maxCapacity) * 100;
    }

    html += '<div class="col-md-4 mb-3">';
    html += '<div class="card">';
    html += '<div class="card-body">';
    html += '<h5>' + w.name + '</h5>';
    html += '<p>' + w.location + '</p>';

    // capacity info
    html += '<p><strong>Capacity:</strong> ' + w.currentCapacity + ' / ' + w.maxCapacity + '</p>';
    html += '<p><strong>Available:</strong> ' + (w.maxCapacity - w.currentCapacity) + '</p>';

    // Simple progress bar
    html += '<div class="progress mb-2" style="height: 20px;">';
    html += '<div class="progress-bar" style="width: ' + percentage + '%">';
    html += Math.round(percentage) + '%';
    html += '</div>';
    html += '</div>';

    html += '<button class="btn btn-sm btn-primary" data-action="edit-warehouse" data-id="' + w.id + '">Edit</button> ';
    html += '<button class="btn btn-sm btn-danger" data-action="delete-warehouse" data-id="' + w.id + '">Delete</button>';
    html += '</div></div></div>';
  }
  html += '</div>';
  
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

// Show/hide transfer form
function showTransferForm() {
  const formDiv = document.getElementById('transferFormDiv');
  formDiv.style.display = 'block';
  
  // Fill warehouse dropdown
  const warehouseSelect = document.getElementById('transferTargetWarehouse');
  let options = '<option value="">Select Warehouse</option>';
  for (let i = 0; i < warehouses.length; i++) {
    options += '<option value="' + warehouses[i].id + '">' + warehouses[i].name + '</option>';
  }
  warehouseSelect.innerHTML = options;
}

function hideTransferForm() {
  document.getElementById('transferFormDiv').style.display = 'none';
  // Clear the form fields
  document.getElementById('transferItemId').value = '';
  document.getElementById('transferQuantity').value = '';
  document.getElementById('transferTargetWarehouse').value = '';
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
  const container = document.getElementById('inventoryListContainer');

  if (inventory.length === 0) {
    container.innerHTML = '<p>No inventory items yet</p>';
    return;
  }
  
  // Sort inventory by warehouse name, then by ID within each warehouse
  let sortedInventory = [];
  for (let i = 0; i < inventory.length; i++) {
    sortedInventory.push(inventory[i]);
  }
  
  sortedInventory.sort((a, b) => {
    if (a.warehouse.name !== b.warehouse.name) {
      return a.warehouse.name.localeCompare(b.warehouse.name);
    }
    return a.id - b.id;
  });
  
  let html = '<table class="table">';
  html += '<thead><tr><th>ID</th><th>SKU</th><th>Name</th><th>Warehouse</th><th>Storage Location</th><th>Quantity</th><th>Actions</th></tr></thead>';
  html += '<tbody>';

  for (let i = 0; i < sortedInventory.length; i++) {
    let item = sortedInventory[i];

    html += '<tr>';
    html += '<td>' + item.id + '</td>';
    html += '<td>' + item.product.sku + '</td>';
    html += '<td>' + item.product.name + '</td>';
    html += '<td>' + item.warehouse.name + '</td>';
    html += '<td>' + item.storageLocation + '</td>';
    html += '<td>' + item.quantity + '</td>';
    html += '<td>';
    html += '<button class="btn btn-sm btn-primary" data-action="edit-inventory" data-id="' + item.id + '">Edit</button> ';
    html += '<button class="btn btn-sm btn-danger" data-action="delete-inventory" data-id="' + item.id + '">Delete</button>';
    html += '</td>';
    html += '</tr>';
  }

  html += '</tbody></table>';
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
      await loadInventory();
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
    destinationWarehouseId: targetWarehouseId,
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
      await loadInventory();
    } else {
      showMessage('Transfer failed!');
    }
  } catch (err) {
    console.error(err);
    showMessage('Error during transfer!');
  }
}

async function loadRecentActivity() {
  try {
    const res = await fetch('http://localhost:8282/activity');
    if (!res.ok) {
      throw new Error('Failed to load activity');
    }
    const activities = await res.json();
    
    const container = document.getElementById('recentActivityContainer');
    
    if (activities.length === 0) {
      container.innerHTML = '<p class="text-muted mb-0">No recent activity</p>';
      return;
    }
    
    let html = '<ul class="list-group list-group-flush">';
    
    // Show only the 10 most recent activities
    const displayCount = Math.min(activities.length, 10);
    for (let i = 0; i < displayCount; i++) {
      const activity = activities[i];
      html += '<li class="list-group-item px-0">';
      html += '<small class="text-muted">' + formatActivityDate(activity.createdAt) + '</small><br>';
      html += '<span>' + activity.description + '</span>';
      html += '</li>';
    }
    
    html += '</ul>';
    container.innerHTML = html;
    
  } catch (err) {
    console.error('Error loading activity:', err);
    document.getElementById('recentActivityContainer').innerHTML = 
      '<p class="text-danger mb-0">Failed to load activity</p>';
  }
}

// Format the date/time for activity display
function formatActivityDate(dateString) {
  const date = new Date(dateString);
  const now = new Date();
  const diffMs = now - date;
  const diffMins = Math.floor(diffMs / 60000);
  const diffHours = Math.floor(diffMs / 3600000);
  const diffDays = Math.floor(diffMs / 86400000);
  
  if (diffMins < 1) {
    return 'Just now';
  } else if (diffMins < 60) {
    return diffMins + ' minute' + (diffMins === 1 ? '' : 's') + ' ago';
  } else if (diffHours < 24) {
    return diffHours + ' hour' + (diffHours === 1 ? '' : 's') + ' ago';
  } else if (diffDays < 7) {
    return diffDays + ' day' + (diffDays === 1 ? '' : 's') + ' ago';
  } else {
    // Format as date for older items
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }
}

// Show reports
async function showReports() {
  // Populate warehouse selector
  const warehouseSelector = document.getElementById('warehouseSelector');
  let options = '<option value="">-- Choose a warehouse --</option>';
  for (let i = 0; i < warehouses.length; i++) {
    options += '<option value="' + warehouses[i].id + '">' + warehouses[i].name + '</option>';
  }
  warehouseSelector.innerHTML = options;
  
  // Populate inventory by warehouse table
  const tableBody = document.getElementById('inventoryByWarehouseTableBody');
  let html = '';
  
  for (let i = 0; i < warehouses.length; i++) {
    let w = warehouses[i];
    let itemCount = 0;
    let totalQty = 0;
    
    for (let j = 0; j < inventory.length; j++) {
      if (inventory[j].warehouse.id === w.id) {
        itemCount++;
        totalQty += inventory[j].quantity;
      }
    }
    
    let capacityPercentage = w.maxCapacity > 0 ? Math.round((w.currentCapacity / w.maxCapacity) * 100) : 0;
    
    html += '<tr>';
    html += '<td>' + w.name + '</td>';
    html += '<td>' + itemCount + '</td>';
    html += '<td>' + totalQty + '</td>';
    html += '<td>' + capacityPercentage + '%</td>';
    html += '</tr>';
  }
  
  tableBody.innerHTML = html;
}

// Load capacity trend for selected warehouse
async function loadCapacityTrend() {
  const warehouseId = document.getElementById('warehouseSelector').value;
  const days = document.getElementById('daysSelector').value;
  const container = document.getElementById('capacityTrendContainer');
  
  if (!warehouseId) {
    container.innerHTML = '<p class="text-warning">Please select a warehouse</p>';
    return;
  }
  
  container.innerHTML = '<p class="text-muted">Loading trend data...</p>';
  
  try {
    const res = await fetch(`http://localhost:8282/capacity-reports/warehouse/${warehouseId}?days=${days}`);
    if (!res.ok) {
      throw new Error('Failed to load capacity trend');
    }
    
    const snapshots = await res.json();
    
    if (snapshots.length === 0) {
      container.innerHTML = '<p class="text-muted">No snapshot data available for this warehouse</p>';
      return;
    }
    
    // Find warehouse name
    let warehouseName = '';
    for (let i = 0; i < warehouses.length; i++) {
      if (warehouses[i].id == warehouseId) {
        warehouseName = warehouses[i].name;
        break;
      }
    }
    
    // Display the data
    let html = '<h6 class="mt-3">Capacity Trend: ' + warehouseName + '</h6>';
    html += '<div class="table-responsive">';
    html += '<table class="table table-sm">';
    html += '<thead>';
    html += '<tr>';
    html += '<th>Date</th>';
    html += '<th>Current Capacity</th>';
    html += '<th>Max Capacity</th>';
    html += '<th>Utilization</th>';
    html += '</tr>';
    html += '</thead>';
    html += '<tbody>';
    
    // Reverse to show most recent first
    for (let i = snapshots.length - 1; i >= 0; i--) {
      const snapshot = snapshots[i];
      const date = new Date(snapshot.snapshotDate);
      const formattedDate = date.toLocaleDateString();
      
      // Color code utilization
      let utilizationClass = '';
      if (snapshot.utilizationPercentage >= 90) {
        utilizationClass = 'text-danger fw-bold';
      } else if (snapshot.utilizationPercentage >= 75) {
        utilizationClass = 'text-warning';
      } else {
        utilizationClass = 'text-success';
      }
      
      html += '<tr>';
      html += '<td>' + formattedDate + '</td>';
      html += '<td>' + snapshot.currentCapacity + '</td>';
      html += '<td>' + snapshot.maxCapacity + '</td>';
      html += '<td class="' + utilizationClass + '">' + snapshot.utilizationPercentage.toFixed(1) + '%</td>';
      html += '</tr>';
    }
    
    html += '</tbody>';
    html += '</table>';
    html += '</div>';
    
    // Add simple text-based trend analysis
    if (snapshots.length >= 2) {
      const oldest = snapshots[0];
      const newest = snapshots[snapshots.length - 1];
      const change = newest.utilizationPercentage - oldest.utilizationPercentage;
      
      html += '<div class="alert alert-info mt-3">';
      html += '<strong>Trend Analysis:</strong> ';
      if (change > 5) {
        html += 'Capacity utilization has <strong>increased</strong> by ' + change.toFixed(1) + '% over this period.';
      } else if (change < -5) {
        html += 'Capacity utilization has <strong>decreased</strong> by ' + Math.abs(change).toFixed(1) + '% over this period.';
      } else {
        html += 'Capacity utilization has remained <strong>relatively stable</strong> over this period.';
      }
      html += '</div>';
    }
    
    container.innerHTML = html;
    
  } catch (err) {
    console.error('Error loading capacity trend:', err);
    container.innerHTML = '<p class="text-danger">Failed to load capacity trend data</p>';
  }
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

// EVENT DELEGATION - Single event listener for all dynamic buttons
function setupEventDelegation() {
  // Handle ALL clicks on the document body
  document.body.addEventListener('click', function(e) {
    const target = e.target;
    const action = target.getAttribute('data-action');
    const id = target.getAttribute('data-id');
    
    // Warehouse actions
    if (action === 'add-warehouse') {
      showWarehouseForm();
    } else if (action === 'edit-warehouse') {
      editWarehouse(parseInt(id));
    } else if (action === 'delete-warehouse') {
      deleteWarehouse(parseInt(id));
    }
    // Inventory actions
    else if (action === 'add-inventory') {
      showInventoryForm();
    } else if (action === 'edit-inventory') {
      editInventory(parseInt(id));
    } else if (action === 'delete-inventory') {
      deleteInventory(parseInt(id));
    } else if (action === 'show-transfer') {
      showTransferForm();
    }
    // Dashboard action
    else if (action === 'goto-warehouses') {
      document.querySelector('.nav-link[data-page="warehouses"]').click();
    }
  });
  
  // Setup form button handlers
  document.getElementById('warehouseFormDiv').addEventListener('click', function(e) {
    if (e.target.matches('button[class*="btn-success"]')) {
      saveWarehouse();
    } else if (e.target.matches('button[class*="btn-secondary"]')) {
      hideWarehouseForm();
    }
  });
  
  document.getElementById('inventoryFormDiv').addEventListener('click', function(e) {
    if (e.target.matches('button[class*="btn-success"]')) {
      saveInventory();
    } else if (e.target.matches('button[class*="btn-secondary"]')) {
      hideInventoryForm();
    }
  });
  
  document.getElementById('transferFormDiv').addEventListener('click', function(e) {
    if (e.target.matches('button[class*="btn-success"]')) {
      submitTransfer();
    } else if (e.target.matches('button[class*="btn-secondary"]')) {
      hideTransferForm();
    }
  });
  
  // Setup report button handler
  const reportBtn = document.getElementById('generateReportBtn');
  if (reportBtn) {
    reportBtn.addEventListener('click', loadCapacityTrend);
  }

}

// Initialize the application
setupNavigation();
setupEventDelegation();
loadWarehouses();
loadInventory();
showDashboard();