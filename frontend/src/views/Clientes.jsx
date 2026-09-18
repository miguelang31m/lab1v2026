import { useEffect, useState } from 'react'
import api from '../services/api'

const emptyForm = { firstName: '', lastName: '', accountNumber: '', balance: '' }

export default function Clientes() {
  const [clientes, setClientes] = useState([])
  const [form, setForm] = useState(emptyForm)
  const [mensaje, setMensaje] = useState('')

  const cargar = async () => {
    try {
      const { data } = await api.get('/customers')
      setClientes(data)
      setMensaje('')
    } catch {
      setMensaje('No fue posible conectar con el backend.')
    }
  }

  useEffect(() => { cargar() }, [])

  const crear = async (e) => {
    e.preventDefault()
    try {
      await api.post('/customers', { ...form, balance: Number(form.balance) })
      setForm(emptyForm)
      setMensaje('Cliente creado correctamente.')
      cargar()
    } catch (error) {
      setMensaje(error.response?.data?.message || 'No fue posible crear el cliente.')
    }
  }

  const eliminar = async (id) => {
    if (!confirm('¿Eliminar este cliente?')) return
    try {
      await api.delete(`/customers/${id}`)
      cargar()
    } catch (error) {
      setMensaje(error.response?.data?.message || 'No fue posible eliminar el cliente.')
    }
  }

  return (
    <section>
      <div className="section-title">
        <div><h2>Consultar clientes</h2><p>Clientes registrados y saldo disponible.</p></div>
      </div>

      <div className="card">
        <form className="grid-form" onSubmit={crear}>
          <input placeholder="Nombre" value={form.firstName} onChange={e => setForm({...form, firstName:e.target.value})} required />
          <input placeholder="Apellido" value={form.lastName} onChange={e => setForm({...form, lastName:e.target.value})} required />
          <input placeholder="Número de cuenta" value={form.accountNumber} onChange={e => setForm({...form, accountNumber:e.target.value})} required />
          <input placeholder="Saldo inicial" type="number" min="0" step="0.01" value={form.balance} onChange={e => setForm({...form, balance:e.target.value})} required />
          <button className="primary" type="submit">Crear cliente</button>
        </form>
        {mensaje && <div className="message">{mensaje}</div>}
      </div>

      <div className="card table-wrap">
        <table>
          <thead><tr><th>ID</th><th>Cuenta</th><th>Nombre</th><th>Saldo</th><th></th></tr></thead>
          <tbody>
            {clientes.map(c => (
              <tr key={c.id}>
                <td>{c.id}</td><td>{c.accountNumber}</td><td>{c.firstName} {c.lastName}</td>
                <td>${Number(c.balance).toLocaleString('es-CO')}</td>
                <td><button className="danger" onClick={() => eliminar(c.id)}>Eliminar</button></td>
              </tr>
            ))}
            {!clientes.length && <tr><td colSpan="5" className="empty">No hay clientes registrados.</td></tr>}
          </tbody>
        </table>
      </div>
    </section>
  )
}
