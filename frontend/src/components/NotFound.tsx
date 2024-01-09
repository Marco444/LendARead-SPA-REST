import {useTranslation} from "react-i18next";

const NotFound = () => {
    const {t} = useTranslation()

    return (<>
        <div className="main-class" style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: '100vh'
        }}>
            <img src="/static/broken_lendaread.png" alt="Animated Image" style={{height: '300px'}}/>
            <span>
                <h1>{t('not_found.title')}</h1>
                <br/>
                <h2>{t('not_found.subtitle')}</h2>
            </span>
        </div>
    </>)
}
export default NotFound