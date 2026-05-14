// src/components/Modal.jsx
import React from 'react';

const Modal = ({ isOpen, onClose, title, children }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-bg open" onClick={(e) => e.target.id === 'modal-overlay' && onClose()} id="modal-overlay">
      <div className="modal">
        <div className="modal-title">
          <span>{title}</span>
          <i className="ti ti-x row-action" onClick={onClose} style={{ cursor: 'pointer' }}></i>
        </div>
        
        <div className="modal-body">
          {children}
        </div>
      </div>
    </div>
  );
};

export default Modal;