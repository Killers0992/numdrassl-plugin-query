![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/Killers0992/numdrassl-plugin-query/total?label=Downloads\&labelColor=2e343e\&color=00FFFF\&style=for-the-badge)

# Numdrassil Plugin Query

A Numdrassil proxy plugin that exposes the Steam Query (A2S) protocol, allowing your server to be queried by standard server browsers and tools.

This enables compatibility with tools like `qstat`, Steam server browsers, and other A2S-compatible services.


> ⚠️ This project is based on the original implementation from  
> https://github.com/G-PORTAL/hytale-plugin-query

---

## ✨ Features

- Supports `A2S_INFO` (Server information)
- Supports `A2S_PLAYER` (Player list)
- Automatically updates player counts
- Configurable host and port
- Lightweight and proxy-friendly

---

## 📦 Installation

1. Download the latest `.jar` from the releases page.
2. Place the `.jar` file inside your proxy `plugins/` folder.
3. Restart the proxy.

---

## ⚙️ Default Configuration

On first startup, the plugin generates:

```yaml
query_host: 0.0.0.0
query_port: 27015
gameserver_port: 5520
server_name: Proxy Server
```

### Configuration Options

| Option | Description |
|--------|------------|
| `query_host` | The IP address the query listener binds to (`0.0.0.0` = all interfaces) |
| `query_port` | Port used for Steam (A2S) query requests |
| `gameserver_port` | Port of the backend game server |
| `server_name` | Name displayed in server queries |

After editing the configuration, restart the proxy to apply changes.

---

## 📡 Usage Example

You can query your server using tools like `qstat`.

### Command

```bash
qstat -a2s 127.0.0.1:27015 -json -R -P
```

### Example Response

```json
[
  {
    "protocol": "a2s",
    "address": "127.0.0.1:27015",
    "status": "online",
    "hostname": "127.0.0.1:27015",
    "name": "Proxy Server",
    "gametype": "numdrassil",
    "map": "world",
    "numplayers": 1,
    "maxplayers": 100,
    "ping": 20,
    "rules": {
      "protocol": "11",
      "gamedir": "numdrassil",
      "dedicated": "1",
      "sv_os": "linux",
      "version": "1.0.0",
      "game_port": "5520",
      "query_port": "27015"
    },
    "players": [
      {
        "name": "PlayerOne",
        "score": 0,
        "time": "10m11s"
      }
    ]
  }
]
```

---
