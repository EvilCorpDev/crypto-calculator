import "./styles.css"
import {Link} from "react-router-dom";

const Header = () => {
    return (
        <div className="header sticky">
            <h2>Crypto Calc</h2>
            <div className="links">
                <div className="link">
                    <Link to="/coins">Coins List</Link>
                </div>
                <div className="link">
                    <Link to="/login">Login</Link>
                </div>
                <div className="link smallImage">
                    <Link to="/profile">
                        <img src="user-icon.png" alt="user icon"/>
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default Header