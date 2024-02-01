import {useContext, useEffect, useState} from "react";
import { useTranslation } from "react-i18next";
import ProfileReviewCard from "../../components/reviews/ProfileReviewCard";
import "../../index.css";
import "../styles/profile.css"
import "../styles/addAsset.css"
import {AuthContext} from "../../contexts/authContext.tsx";
import useReviews, {ReviewApi} from "../../hooks/reviews/useReviews.ts";
import UserProfile from "../../components/userDetails/UserProfile.tsx";
import UserProfileTabs from "../../components/userDetails/UserProfileTabs.tsx";

const ProfileView = ({isCurrentUser, id}) => {

    const { t } = useTranslation();
    const {userImage, userDetails, user} = useContext(AuthContext)
    const [selectedTab, setSelectedTab] = useState("lender_reviews")
    const {lenderReviews, borrowerReviews, fetchReviews} = useReviews()

    useEffect(() => {
        fetchReviews().then()
    }, [id])


    return (
        <div className="main-class">
            <div className="user-container">
                <div className="info-container w-100 mt-10" id="user-info">
                   <UserProfile isCurrentUser={isCurrentUser}/>
                    <hr />
                    <div className="tabs-container">
                    <UserProfileTabs selectedTab={selectedTab} setSelectedTab={setSelectedTab}/>
                    </div>
                    <div className="tab-content" >
                        {selectedTab === "lender_reviews" &&
                            (lenderReviews.length > 0
                                ? lenderReviews.map((review: ReviewApi) => <ProfileReviewCard review={review} user={user} userName={userDetails.userName}/>)
                                : <p>No Lender reviews yet</p>
                        )}
                        {selectedTab === "borrower_reviews" &&
                            (borrowerReviews.length > 0
                                ? borrowerReviews.map((review: ReviewApi) => <ProfileReviewCard review={review} user={user} userName={userDetails.userName}/>)
                                : <p>No Borrower reviews yet</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ProfileView;
