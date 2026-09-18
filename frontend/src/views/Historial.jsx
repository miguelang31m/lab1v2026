import { useEffect, useState } from 'react'
import api from '../services/api'

export default function Historial() {
  const [clientes, setClientes] = useState([])
  const [cuenta, setCuenta] = useState('')
  const [transacciones, setTransacciones] = useState([])
  const [mensaje, setMensaje] = useState('')

  useEffect(() => {
    api.get('/customers').then(r => setClientes(r.data)).catch(() => setMensaje('No fue posible cargar los clientes.'))
  }, [])

  const consultar = async (e) => {
    e.preventDefault()
    if (!cuenta) return
    try {
      const { data } = await api.get(`/transactions/account/${cuenta}`)
      setTransacciones(data)
      setMensaje(data.length ? '' : 'Este cliente todavía no tiene transacciones.')
    } catch {
      setMensaje('No fue posible consultar el histórico.')
    }
  }

  return (
    <section>
      <div className="section-title"><div><h2>Histórico de transacciones</h2><p>Consulta movimientos enviados o recibidos por cliente.</p></div></div>
      <div className="card">
        <form className="history-search" onSubmit={consultar}>
          <select value={cuenta} onChange={e => setCuenta(e.target.value)} required>
            <option value="">Seleccione un cliente...</option>
            {clientes.map(c => <option key={c.id} value={c.accountNumber}>{c.accountNumber} - {c.firstName} {c.lastName}</option>)}
          </select>
          <button className="primary" type="submit">Consultar</button>
        </form>
        {mensaje && <div className="message">{mensaje}</div>}
      </div>

      <div className="card table-wrap">
        <table>
          <thead><tr><th>ID</th><th>Origen</th><th>Destino</th><th>Valor</th><th>Fecha</th></tr></thead>
          <tbody>
            {transacciones.map(t => <tr key={t.id}>
              <td>{t.id}</td><td>{t.senderAccountNumber}</td><td>{t.receiverAccountNumber}</td>
              <td>${Number(t.amount).toLocaleString('es-CO')}</td>
              <td>{t.timestamp ? new Date(t.timestamp).toLocaleString('es-CO') : '-'}</td>
            </tr>)}
            {!transacciones.length && <tr><td colSpan="5" className="empty">Seleccione un cliente para consultar.</td></tr>}
          </tbody>
        </table>
      </div>
    </section>
  )
}
