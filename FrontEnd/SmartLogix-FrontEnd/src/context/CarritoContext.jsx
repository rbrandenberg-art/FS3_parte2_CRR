import { createContext, useContext, useState, useEffect } from 'react';

const CarritoContext = createContext();

export const CarritoProvider = ({ children }) => {
    // 1. Inicializar desde localStorage igual que el usuario
    const [carrito, setCarrito] = useState(() => {
        const guardado = localStorage.getItem('carrito_smartlogix');
        return guardado ? JSON.parse(guardado) : [];
    });

    // 2. Guardar automáticamente cada vez que el carrito cambie
    useEffect(() => {
        localStorage.setItem('carrito_smartlogix', JSON.stringify(carrito));
    }, [carrito]);

    const agregarAlCarrito = (producto) => {
        setCarrito((prev) => {
            const existe = prev.find((p) => p.id === producto.id);
            if (existe) {
                return prev.map((p) =>
                    p.id === producto.id ? { ...p, cantidad: p.cantidad + 1 } : p
                );
            }
            return [...prev, { ...producto, cantidad: 1 }];
        });
    };

    const quitarDelCarrito = (id) => {
        setCarrito((prev) => prev.filter((p) => p.id !== id));
    };

    const cambiarCantidad = (id, cantidad) => {
        if (cantidad <= 0) return quitarDelCarrito(id);
        setCarrito((prev) =>
            prev.map((p) => (p.id === id ? { ...p, cantidad } : p))
        );
    };

    const vaciarCarrito = () => setCarrito([]);

    const total = carrito.reduce((acc, p) => acc + p.precio * p.cantidad, 0);
    const totalItems = carrito.reduce((acc, p) => acc + p.cantidad, 0);

    return (
        <CarritoContext.Provider value={{ carrito, agregarAlCarrito, quitarDelCarrito, cambiarCantidad, vaciarCarrito, total, totalItems }}>
            {children}
        </CarritoContext.Provider>
    );
};

export const useCarrito = () => useContext(CarritoContext);