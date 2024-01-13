import React, {useState} from 'react';
import { useTranslation } from 'react-i18next';
import {isPublic} from "./LendedBooksOptions.tsx";
import ChangeStatusModal from "../modals/ChangeStatusModal.tsx";

function AssetOptionsMenu({ asset, haveActiveLendings }) {
    const { t } = useTranslation();

    const [showModalVisibility, setShowModalVisibility] = useState(false);
    const [showModalEdit, setShowModalEdit] = useState(false);
    const [showModalDelete, setShowModalDelete] = useState(false);
    const [showModalReservable, setShowModalReservable] = useState(false)

    const handleSubmitVisibilityModal = async () => {
        setShowModalVisibility(false)
    }

    const handleEditAsset = () => {

    }

    return (
        <div style={{
            backgroundColor: '#f0f5f0',
            padding: '10px',
            borderRadius: '20px',
            width: '50%',
            display: "flex",
            alignContent: "center",
        }} className="flex-column">
        <h3>Lended Book actions</h3>
        <div className="options-menu m-auto">
            <div className="d-flex flex-row">
            {!(asset.reservable && haveActiveLendings) && (
                <button id="privatePublicBtn" className="btn btn-green m-1" onClick={() => setShowModalVisibility(true)}>
                    {isPublic(asset.status) ? (
                        <>
                            <i className="fas fa-eye-slash fa-lg"  ></i> {t('userBookDetails.makePrivate')}
                        </>
                    ) : (
                        <>
                            <i className="fas fa-eye fa-lg" ></i> {t('userBookDetails.makePublic')}
                        </>
                    )}
                </button>
            )}

            {!haveActiveLendings && (
                <button id="changeIsReservable" className="btn btn-green m-1" style={{ marginTop: '5px' }} onClick={() => setShowModalReservable(true)}>
                    {asset.reservable ? (
                        <>
                            <i className="fas fa-calendar-times"></i> {t('not_reservable')}
                        </>
                    ) : (
                        <>
                            <i className="fas fa-calendar-alt"></i> {t('reservable')}
                        </>
                    )}
                </button>
            )}  </div>
            <div className="d-flex flex-row">
                <button className="btn btn-green m-1"  style={{ marginTop: '5px', textDecoration: 'none' }} onClick={() => setShowModalEdit(true)}>
                    <i className="fas fa-pencil-alt"></i>
                    {t('edit')}
                </button>
                <button id="deleteBtn" className="btn btn-red m-1" style={{ marginTop: '5px' }} onClick={() => setShowModalDelete(true)}>
                    <i className="fas fa-trash"></i>
                    {t('delete')}
                </button>
            </div>
            {/* Include modal components here */}
            {/* <DeleteBookModal asset={asset} />
      <ChangeReservabilityModal asset={asset} /> */}
            <ChangeStatusModal handleSubmitModal={handleSubmitVisibilityModal} asset={asset} showModal={showModalVisibility} handleCloseModal={() => setShowModalVisibility(false)} />
        </div>
        </div>
    );
}

export default AssetOptionsMenu;