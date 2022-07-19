import {useState} from "react";
import "./styles.css"
import Cookies from "universal-cookie/lib";
import {useNavigate} from "react-router-dom";

const Login = () => {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')

    const navigate = useNavigate()

    const handleSubmit = (event) => {
        event.preventDefault();
        fetch("/auth", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({username, password})
        }).then(resp => {
            if (resp.ok) {
                return resp.json()
            } else {
                console.log(resp.statusMessage)
            }
        }).then(data => {
            const cookies = new Cookies();
            cookies.set('token', data.token, {path: '/'})

            navigate("/coins")
        })
    }

    return (
        <div className="container">
            <form className="login">
                <div className="loginElement">
                    <input type="text" placeholder="Username" value={username} onChange={(event) => {
                        setUsername(event.target.value)
                    }}/>
                </div>
                <div className="loginElement">
                    <input type="password" placeholder="Password" value={password} onChange={(event) => {
                        setPassword(event.target.value)
                    }}/>
                </div>
                <div className="loginElement">
                    <button onClick={(event) => handleSubmit(event)}>Login</button>
                </div>
            </form>
        </div>
    )
}

export default Login