import WS from 'ws';
import fs from 'fs/promises';
import path from 'path';
import watch from 'node-watch';


interface Client {
  ws: WS,
  authenticated: boolean;
  username: string;
}

interface MapPoint {
  x: number,
  z: number,
  type: string,
  name: string,
  dim: string,
}

async function exists(path: string) {
  try {
    await fs.access(path)
    return true
  } catch {
    return false
  }
}

async function createIfNotExists(path: string) {
  let fileExists = await exists(path);
  if (!fileExists) {
    await fs.writeFile(path, '');
  }
}

const wss = new WS.Server({ port: 31664 });
const clients: Map<WS, Client> = new Map();
const dataDir = process.argv[2];
const usersFile = path.join(dataDir, 'users.json');
const historyFile = path.join(dataDir, 'history.txt');
const aliasesFile = path.join(dataDir, 'aliases.txt');

let users: { [authId: string]: string } = {};
let mapPoints: MapPoint[] = [];
let aliases: { [alias: string]: string } = {};



async function main() {
  await createIfNotExists(usersFile);
  await createIfNotExists(historyFile);
  await createIfNotExists(aliasesFile);

  updateUsers();
  updatePoints();
  updateAliases();

  watch(usersFile).on('change', updateUsers);

  watch(historyFile).on('change', updatePoints);

  watch(aliasesFile).on('change', updateAliases);

  wss.on('connection', async function connection(ws) {
    clients.set(ws, { ws, authenticated: false, username: '' });
    console.log("NEWCLIENT", clients.get(ws));
    ws.on('message', async function incoming(message) {
      const data = JSON.parse(message.toString());
      const client = clients.get(ws);
      console.log("MESSAGE", data);
      if (!client || !data || !data.type) {
        return;
      }
      if (data.authID && users[data.authID]) {
        let id = data.authID;
        client.authenticated = true;
        client.username = users[id];
        console.log('AUTHENTICATING', id, users[id]);

      }
      if (data.type === 'GET' && client.authenticated) {
        console.log('GET');
        ws.send(JSON.stringify({
          type: 'POINTS',
          data: mapPoints,
        }))
      }
    });
  });
}

let updateUsers = async () => {
  let file = (await fs.readFile(usersFile)).toString();
  users = JSON.parse(file);
}

let updatePoints = async () => {
  let file = (await fs.readFile(historyFile)).toString();
  let oldPoints = JSON.stringify(mapPoints);
  mapPoints = [...file.matchAll(/@map(.*?)(\(.*\))? ?([0-9\-]+) ?([0-9\-]+) ?(e|n|o|)/g)].map(v => ({
    x: +(v[3].trim()),
    z: +(v[4].trim()),
    type: v[2]
      ? (aliases[v[2].trim().slice(1, -1).trim()]
        ? aliases[v[2].trim().slice(1, -1).trim()]
        : v[2].trim().slice(1, -1).trim())
      : '',
    name: v[1]?.trim() || '',
    dim: v[5] === 'e' ? 'E' : (v[5] === 'n' ? 'N' : 'O'),
  }));

  if (JSON.stringify(mapPoints) !== oldPoints) {
    [...clients.values()].forEach(c => {
      if (c.authenticated) {
        c.ws.send(JSON.stringify({
          type: 'POINTS',
          data: mapPoints,
        }))
      }
    })
  }
}

let updateAliases = async () => {
  let file = (await fs.readFile(aliasesFile)).toString();
  aliases = Object.fromEntries(file.split('\n').map(v => v.split('=').map(v2 => v2.trim())));
  updatePoints();
}

main();
