# React + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react/README.md) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

# Running the container from the image

## Building the image 

```
docker build . -t react-frontend
```

## Running the image

```
docker run --rm --name react-frontend-srv -p 3000:5173 {name-of-the-image}
```

`-- rm` - removes the container as it exists

`-- name` - name of the container

`-p` - the port where we want to expose [host]:[internal], host port maps to internal port.

`-d` - detached mode, which enables the command:

```
docker exec -it {container-name} sh
```

or bash, and will allow to operate inside the running container.


