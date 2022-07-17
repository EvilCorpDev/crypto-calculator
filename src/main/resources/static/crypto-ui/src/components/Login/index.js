import {useState} from "react";
import "./styles.css"

const Login = () => {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')

    const handleSubmit = (event) => {
        event.preventDefault();
        console.log({username, password})
    }

    return (
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
    )
}

export default Login