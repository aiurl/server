## Build Docker image

```bash
docker build -t theurl.io/config -f config/Dockerfile .
```

## Run Docker container

```bash
docker run -d --restart always --name linkyou-config-server -p 8900:8900 -v $(pwd)/config:/app/config theurl.io/config
```
