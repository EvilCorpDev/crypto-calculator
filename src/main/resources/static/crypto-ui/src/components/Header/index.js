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
            </div>
        </div>
    );
}

export default Header