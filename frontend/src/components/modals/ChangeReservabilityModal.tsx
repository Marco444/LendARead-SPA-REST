import  { useState } from 'react';
import { useTranslation } from 'react-i18next';
import {isPrivate, isPublic} from "../user/LendedBooksOptions.tsx";

function ChangeReservabilityModal({ asset, showModal, handleCloseModal, handleSubmitModal }) {
    const { t } = useTranslation();

    return (
        <>
            <div className={`modal ${showModal ? 'show' : ''}`}  role="dialog" aria-labelledby="modalTitle">
                <div className="modal-dialog modal-content" style={{
                    backgroundColor: '#f0f5f0',
                    borderRadius: '20px',
                }}>
                    <div className="modal-header">
                        {asset.reservable ? (
                                <i className="fas fa-calendar-times"></i>
                        ) : (
                                <i className="fas fa-calendar-alt"></i>
                        )}
                        <h2 className="modal-title" id="modalTitle">
                            {t('userHomeView.changeReservability')}
                        </h2>

                        <button onClick={handleCloseModal} className="btn btn-secondary">
                            <i className="fas fa-window-close fa-lg"></i>
                        </button>
                    </div>
                    <div className="modal-body">
                        <p>
                            {asset.reservable ? t('userHomeView.changeReservabilityNo'): t('userHomeView.changeReservabilityYes') }
                        </p>
                    </div>
                    <button type="submit" className="btn btn-green" onClick={handleSubmitModal}>
                        {asset.reservable ? t('userHomeView.makeReservableNo') : t('userHomeView.makeReservableYes')}
                    </button>
                </div>
            </div>
        </>
    );
}

export default ChangeReservabilityModal;
