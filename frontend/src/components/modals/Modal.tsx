
function Modal({
                   showModal = true,
                   handleCloseModal = () => {},
                   handleSubmitModal = () => {},
                   errorType = false,
                    title = "",
                    subtitle = "",
                    btnText = ""
}) {

    return (
        <>
            <div className={`modal ${showModal ? 'show' : ''}`}  role="dialog" aria-labelledby="modalTitle">
                <div className="modal-dialog modal-content" style={{
                    backgroundColor: '#f0f5f0',
                    borderRadius: '20px',
                }}>
                    <div className="modal-header">
                        <h2 className="modal-title" id="modalTitle">
                            {title}
                        </h2>

                        <button onClick={handleCloseModal} className="btn btn-secondary">
                            <i className="fas fa-window-close fa-lg"></i>
                        </button>
                    </div>
                    <div className="modal-body">
                        <p>
                            {subtitle}
                        </p>
                    </div>
                    <button type="submit" className={`btn ${errorType ? 'btn-red' : 'btn-green'}`} onClick={handleSubmitModal}>
                        {btnText}
                    </button>

                </div>
            </div>
        </>
    );
}

export default Modal;