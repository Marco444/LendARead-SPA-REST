import { useTranslation } from 'react-i18next';
import useAssetInstances, {
    checkCanceled,
    checkFinished,
    checkRejected
} from '../../hooks/assetInstance/useUserAssetInstances.ts';
import {useContext, useEffect} from "react";
import {AuthContext} from "../../contexts/authContext.tsx";
import useUserAssetInstances from "../../hooks/assetInstance/useUserAssetInstances.ts";
import LoadingAnimation from "../LoadingAnimation.tsx";
import LoadingAnimationWhite from "../LoadingAnimationWhite.tsx";
import Pagination from "../Pagination.tsx"; // Path to your custom hook

const LendedBooksTable = ({isLender, handleRowClicked}) => {
    const { t } = useTranslation();
    const { setFilter, filter, fetchLendings, sort, setSort, currentPage, changePage, totalPages, books, isLoading} = useUserAssetInstances();

    useEffect(() => {
        fetchLendings(currentPage, sort, filter, isLender).then()
    }, [])

    const handleFilterChange = async(newFilter: string) => {
        setFilter(newFilter);
        await fetchLendings(currentPage, sort, newFilter, isLender)
    };

    const handleSortChange = async (column: string) => {
        const isAsc = sort.column === column && sort.order === 'ASCENDING';
        const newSort = { column, order: isAsc ? 'DESCENDING' : 'ASCENDING' }
        setSort(newSort);
        await fetchLendings(currentPage, newSort, filter, isLender)
    };

    const renderSortIcon = (column: string) => {
        return sort.column === column ? (sort.order === 'ASCENDING' ? '↑' : '↓') : '';
    };

    const buttonStyle = (filter_: string) => {
        if(filter === filter_)
            return {
                backgroundColor:  "#7f8d7f",
                color: 'white',
                border: 'gray',
                fontWeight: 'bold'
            }
        else
            return {
            backgroundColor:  '#d2e1d2',
            color: 'white',
            border: 'gray',
            fontWeight: 'bold'
        }

    }

    const paginationStyle = {
        color: "gray",
        border: 'none',
        borderRadius: '20px'
    }

    return (
        <>
            {isLoading ?
                <LoadingAnimationWhite /> :
        <div className="container mt-3">
            <div className="d-flex justify-content-between align-items-center">
                <h2 className="m-1">{isLender ? t('lended_books'): t('borrowed_books')}</h2>
                <div className="btn-group">
                    <button style={buttonStyle('all')} onClick={() => handleFilterChange('all')} className="btn btn-outline-primary">{t('all')}</button>
                    <button style={buttonStyle('pending')}  onClick={() => handleFilterChange('pending')} className="btn btn-outline-primary">{t('pending')}</button>
                    <button style={buttonStyle('delivered')}  onClick={() => handleFilterChange('delivered')} className="btn btn-outline-primary">{t('delivered')}</button>
                    <button style={buttonStyle('canceled')}  onClick={() => handleFilterChange('canceled')} className="btn btn-outline-primary">{t('canceled')}</button>
                    <button style={buttonStyle('rejected')}  onClick={() => handleFilterChange('rejected')} className="btn btn-outline-primary">{t('rejected')}</button>
                    <button style={buttonStyle('finished')}  onClick={() => handleFilterChange('finished')} className="btn btn-outline-primary">{t('finished')}</button>
                </div>
            </div>
            <table className="table table-hover mt-2 mb-3">
                <thead className="table-light">
                <tr>
                    <th scope="col" onClick={() => handleSortChange('image')}>{t('image')} {renderSortIcon('image')}</th>
                    <th scope="col" onClick={() => handleSortChange('title')}>{t('title')} {renderSortIcon('title')}</th>
                    <th scope="col" onClick={() => handleSortChange('start_date')}>{t('start_date')} {renderSortIcon('start_date')}</th>
                    <th scope="col" onClick={() => handleSortChange('return_date')}>{t('return_date')} {renderSortIcon('return_date')}</th>
                    <th scope="col" onClick={() => handleSortChange('user')}>{t('user')} {renderSortIcon('user')}</th>
                    <th scope="col" onClick={() => handleSortChange('state')}>{t('state')} {renderSortIcon('state')}</th>
                </tr>
                </thead>
                <tbody>
                {books.length === 0 ? (
                    <tr>
                        <td colSpan={5} className="text-center">
                            <h5>{t('no_books_available')}</h5>
                        </td>
                    </tr>
                ) : (
                    books.map((book, index) => (
                        <tr
                            key={index}
                            onClick={() => handleRowClicked(book, isLender ? "lended" : "borrowed")}
                            style={{
                                cursor: "pointer",
                                opacity: checkFinished(book) || checkRejected(book) || checkCanceled(book) ? 0.6 : 1
                            }}
                        >                            <td>
                                <img style={{height: '125px', width: '75px', objectFit: 'cover'}} src={book.imageUrl} alt={book.title}/>
                            </td>
                            <td>{book.title}</td>
                            <td>{book.start_date}</td>
                            <td>{book.return_date}</td>
                            <td>{book.user}</td>
                            <td>{t(`${book.lendingStatus.toLowerCase()}`)}</td>
                        </tr>
                    ))
                )}
            </tbody>
        </table>
           <Pagination  currentPage={currentPage} totalPages={totalPages} changePage={changePage}/>
            </div>
            }
        </>
    );
};

export default LendedBooksTable;