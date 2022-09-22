import {useEffect, useState} from "react";

const useFetch = (url, headers = {}) => {
    const [data, setData] = useState(null);
    const [pending, setPending] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch(url, {
            headers: headers
        }).then(res => {
            if (!res.ok) {
                throw Error(`Couldn't fetch data for ${url}`)
            }
            return res.json()
        }).then(data => {
            setData(data);
            setPending(false);
            setError(null);
        }).catch(err => {
            setPending(false)
            setError(err.message);
        })
    }, [])

    return {data, setData, error, pending};
}

export default useFetch