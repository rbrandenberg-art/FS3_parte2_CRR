// src/hooks/useCatalogo.js
import { useState, useEffect } from 'react';
import { getInventario } from '../api/api';

const MOCK_PRODUCTOS = [
    { id: 1, nombre: 'Laptop Pro X', descripcion: 'Laptop de alto rendimiento para profesionales', precio: 1299990, stock: 5, categoria: 'Computación' },
    { id: 2, nombre: 'Monitor UltraWide', descripcion: 'Monitor 34" curvo para máxima productividad', precio: 599990, stock: 8, categoria: 'Periféricos' },
    { id: 3, nombre: 'Teclado Mecánico RGB', descripcion: 'Teclado mecánico con switches Cherry MX', precio: 89990, stock: 15, categoria: 'Periféricos' },
    { id: 4, nombre: 'Mouse Inalámbrico', descripcion: 'Mouse ergonómico con 90 días de batería', precio: 49990, stock: 20, categoria: 'Periféricos' },
    { id: 5, nombre: 'Auriculares Noise Cancelling', descripcion: 'Sonido premium con cancelación activa de ruido', precio: 249990, stock: 3, categoria: 'Audio' },
    { id: 6, nombre: 'Webcam 4K', descripcion: 'Cámara web 4K para videollamadas profesionales', precio: 129990, stock: 0, categoria: 'Accesorios' },
];

export const useCatalogo = () => {
    const [productos, setProductos] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchProductos = async () => {
            try {
                const res = await getInventario();
                // Axios guarda la respuesta del backend en la propiedad .data
                setProductos(res.data);
            } catch (error) {
                console.warn("No se pudo conectar al backend. Cargando MOCK_PRODUCTOS.", error);
                setProductos(MOCK_PRODUCTOS);
            } finally {
                setLoading(false);
            }
        };
        fetchProductos();
    }, []);

    return { productos, loading };
};