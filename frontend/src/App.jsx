import { useState } from 'react'
import Clientes from './views/Clientes'
import Transferencia from './views/Transferencia'
import Historial from './views/Historial'

export default function App() {
  const [view, setView] = useState('clientes')

  return (
    <div className="app-shell">
      <header className="topbar glass-panel">
        <div className="brand-block">
          <span className="pixel-tag">LAB 01 / 2026</span>
          <h1>Banco UdeA</h1>
          <p>Arquitectura de Software</p>
        </div>

        <nav className="nav-tabs" aria-label="Navegación principal">
          <button className={view === 'clientes' ? 'active' : ''} onClick={() => setView('clientes')}>Clientes</button>
          <button className={view === 'transferencia' ? 'active' : ''} onClick={() => setView('transferencia')}>Transferencia</button>
          <button className={view === 'historial' ? 'active' : ''} onClick={() => setView('historial')}>Histórico</button>
        </nav>
      </header>

      <main>
        {view === 'clientes' && <Clientes />}
        {view === 'transferencia' && <Transferencia />}
        {view === 'historial' && <Historial />}
      </main>

      <footer className="footer-note">SPRING BOOT + REACT</footer>
    </div>
  )
}
