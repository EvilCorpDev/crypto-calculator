import {useState} from "react";
import Cookies from "universal-cookie/lib";
import useFetch from "../../useFetch";
import "./styles.css";


const UserPage = () => {
    const cookies = new Cookies();
    const {
        data: user,
        error,
        pending
    } = useFetch("/users/current", {'Authorization': `Bearer ${cookies.get('token')}`});

    const [username, setUsername] = useState(user === null ? '' : user.username);
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [binanceToken, setBinanceToken] = useState('');
    const [secretKey, setSecretKey] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        fetch("/users", {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Authorization':  `Bearer ${cookies.get('token')}`
            },
            body: JSON.stringify({username, oldPassword, newPassword})
        }).then(resp => {
            if (resp.ok) {
                console.log(resp.json());
            } else {
                console.log(resp.statusMessage)
            }
        })
    }

    const handleConnectBinance = (event) => {
        event.preventDefault();
        fetch("/users/token", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization':  `Bearer ${cookies.get('token')}`
            },
            body: JSON.stringify({apiToken: binanceToken, secretKey})
        }).then(resp => {
            if (resp.ok) {
                console.log(resp);
            } else {
                console.log(resp.statusMessage)
            }
        })
    }

    return (
        <div className="container">
            {user && <div className="userProfile">
                <form className="profile">
                    <div className="profileElement">
                        <input type="text" placeholder="Username" value={username} onChange={(event) => {
                            setUsername(event.target.value)
                        }}/>
                    </div>
                    <div className="profileElement">
                        <input type="password" placeholder="Old Password" value={oldPassword} onChange={(event) => {
                            setOldPassword(event.target.value)
                        }}/>
                    </div>
                    <div className="profileElement">
                        <input type="password" placeholder="New Password" value={newPassword} onChange={(event) => {
                            setNewPassword(event.target.value)
                        }}/>
                    </div>
                    <div className="profileElement">
                        <input type="text" placeholder="Binance Token" value={binanceToken} onChange={(event) => {
                            setBinanceToken(event.target.value)
                        }}/>
                        <input type="text" placeholder="Binance Secret Key" value={secretKey} onChange={(event) => {
                            setSecretKey(event.target.value)
                        }}/>
                        <button onClick={(event) => handleConnectBinance(event)}>Connect to Binance</button>
                    </div>
                    <div className="profileElement saveButton">
                        <button onClick={(event) => handleSubmit(event)}>Save</button>
                    </div>
                </form>
            </div>}
        </div>
    );
}

export default UserPage