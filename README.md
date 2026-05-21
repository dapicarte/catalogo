┌─────────────────────────┐                                                          
│ProductoDTO              │                                                          
├─────────────────────────┤  ┌──────────────────────────┐   ┌───────────────────────┐
│- idProducto: Long       │  │CarritoDTO                │   │UsuarioDTO             │
│- nombreProducto: String │  ├──────────────────────────┤   ├───────────────────────┤
│- cantidad: Integer      │  │- idCarrito: Long         │   │- id: Long             │
│- precioUnitario: Integer│  │- idUsuario: Long         │   │- telefono: int        │
│                         │--│- fechaCreacion: LocalDate│---│- fechaRegistro: String│
│+ crearProducto()        │  │- subtotal: Double        │   │                       │
│+ listarProductos()      │  │- total: Double           │   │+ historialPedidos()   │
│+ eliminarProducto()     │  │                          │   │+ agregarDireccion()   │
│+ actualizarStock()      │  │+ method(type): type      │   └───────────────────────┘
│+ calcularTotal()        │  └──────────────────────────┘                            
└─────────────────────────┘                |                                         
                                           |                                         
                                           |                                         
                                 ┌───────────────────┐                               
                                 │Pedido             │   ┌────────────────────┐      
                                 ├───────────────────┤   │VentaDTO            │      
                                 │- idPedido: Long   │   ├────────────────────┤      
                                 │- idCarrito: Long  │   │- id: Long          │      
                                 │- idVenta: Long    │   │- fechaVenta: String│      
                                 │- fecha: String    │---│- total: Double     │      
                                 │- estado: boolean  │   │                    │      
                                 │- total: int       │   │+ listarVenta()     │      
                                 │                   │   │+ generarBoleta()   │      
                                 │+ confirmarPedido()│   │+ generarFactura()  │      
                                 │+ cancelarPedido() │   └────────────────────┘      
                                 └───────────────────┘  