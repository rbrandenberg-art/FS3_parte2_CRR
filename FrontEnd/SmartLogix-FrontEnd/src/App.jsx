// src/App.jsx
import InventarioList from './components/InventarioList';

function App() {
  return (
    <div className="App">
      <nav className="bg-blue-900 text-white p-4 shadow-lg">
        <div className="container mx-auto font-bold text-xl">SmartLogix System</div>
      </nav>
      <InventarioList />
    </div>
  );
}

export default App;