import { useEffect, useState } from 'react'
import api from '../services/api'

export default function Transferencia() {
  const [clientes, setClientes] = useState([])
  const [form, setForm] = useState({ senderAccountNumber:'', receiverAccountNumber:'', amount:'' })
  const [mensaje, setMensaje] = useState('')

  useEffect(() => {
    api.get('/customers').then(r => setClientes(r.data)).catch(() => setMensaje('No fue posible cargar los clientes.'))
  }, [])

  const transferir = async (e) => {
    e.preventDefault()
    try {
      await api.post('/transactions/transfer', { ...form, amount: Number(form.amount) })
      setMensaje('Transferencia realizada correctamente.')
      setForm({ senderAccountNumber:'', receiverAccountNumber:'', amount:'' })
    } catch (error) {
      setMensaje(error.response?.data?.message || 'No fue posible realizar la transferencia.')
    }
  }

  return (
    <section>
      <div className="section-title"><div><h2>Realizar transferencia</h2><p>Transfiere dinero entre dos cuentas registradas.</p></div></div>
      <div className="card narrow">
        <form className="stack-form" onSubmit={transferir}>
          <label>Cuenta origen
            <select value={form.senderAccountNumber} onChange={e => setForm({...form, senderAccountNumber:e.target.value})} required>
              <option value="">Seleccione...</option>
              {clientes.map(c => <option key={c.id} value={c.accountNumber}>{c.accountNumber} - {c.firstName} {c.lastName}</option>)}
            </select>
          </label>
          <label>Cuenta destino
            <select value={form.receiverAccountNumber} onChange={e => setForm({...form, receiverAccountNumber:e.target.value})} required>
              <option value="">Seleccione...</option>
              {clientes.map(c => <option key={c.id} value={c.accountNumber}>{c.accountNumber} - {c.firstName} {c.lastName}</option>)}
            </select>
          </label>
          <label>Valor
            <input type="number" min="0.01" step="0.01" value={form.amount} onChange={e => setForm({...form, amount:e.target.value})} required />
          </label>
          <button className="primary" type="submit">Realizar transferencia</button>
        </form>
        {mensaje && <div className="message">{mensaje}</div>}
      </div>
    </section>
  )
}
