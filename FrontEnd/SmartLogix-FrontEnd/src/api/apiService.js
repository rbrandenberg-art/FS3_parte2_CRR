// src/api/apiService.js
const API_URL = 'http://localhost:8080';

export const apiFetch = async (endpoint, opts = {}) => {
  try {
    const response = await fetch(`${API_URL}${endpoint}`, {
      headers: { 'Content-Type': 'application/json' },
      ...opts
    });
    if (!response.ok) throw new Error(response.status);
    return await response.json();
  } catch (error) {
    console.error("Error en la petición:", error);
    return null;
  }
};