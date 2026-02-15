const output = document.getElementById('output');
const authStatus = document.getElementById('auth-status');

const show = (title, data) => {
  output.textContent = JSON.stringify({ title, data }, null, 2);
};

const parseFiles = (value) => value
  .split(',')
  .map((item) => item.trim())
  .filter(Boolean);

async function api(path, options = {}) {
  const config = {
    credentials: 'include',
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options
  };

  const response = await fetch(path, config);
  const raw = await response.text();
  let parsed;
  try {
    parsed = raw ? JSON.parse(raw) : null;
  } catch {
    parsed = raw;
  }

  if (!response.ok) {
    throw new Error(typeof parsed === 'string' ? parsed : JSON.stringify(parsed));
  }

  return parsed;
}

async function run(title, executor) {
  try {
    const data = await executor();
    show(title, data ?? { status: 'ok' });
  } catch (error) {
    show(`${title}: error`, error.message);
  }
}

document.getElementById('clear-output').addEventListener('click', () => {
  output.textContent = '{\n  "status": "ready"\n}';
});

document.getElementById('login-form').addEventListener('submit', async (event) => {
  event.preventDefault();
  const form = new FormData(event.target);
  const params = new URLSearchParams();
  params.set('username', form.get('username'));
  params.set('password', form.get('password'));

  await run('login', () => fetch('/login', {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: params.toString()
  }).then((response) => {
    if (!response.ok && response.status !== 302) {
      throw new Error('Login failed');
    }
    authStatus.textContent = 'Авторизован';
    return { status: 'ok' };
  }));
});

document.getElementById('logout-btn').addEventListener('click', () => {
  run('logout', () => fetch('/logout', { method: 'POST', credentials: 'include' }).then(() => {
    authStatus.textContent = 'Не авторизован';
    return { status: 'ok' };
  }));
});

document.querySelector('[data-action="load-documents"]').addEventListener('click', () => {
  const page = document.getElementById('documents-page').value || '0';
  run('documents/all', () => api(`/documents/all?page=${page}`));
});

document.getElementById('create-document-form').addEventListener('submit', (event) => {
  event.preventDefault();
  const form = new FormData(event.target);
  const payload = {
    directoryId: Number(form.get('directoryId')),
    type: form.get('type') || null,
    documentPriority: form.get('documentPriority'),
    currentVersion: {
      title: form.get('title'),
      description: form.get('description'),
      files: parseFiles(form.get('files') || '')
    }
  };

  run('documents:create', () => api('/documents', { method: 'POST', body: JSON.stringify(payload) }));
});

document.querySelector('[data-action="load-directory"]').addEventListener('click', () => {
  const id = document.getElementById('directory-id').value;
  run('directories/get', () => api(`/directories/${id}/all`));
});

document.getElementById('create-directory-form').addEventListener('submit', (event) => {
  event.preventDefault();
  const form = new FormData(event.target);
  const parentId = form.get('directoryId');
  const payload = {
    directoryId: parentId ? Number(parentId) : null,
    title: form.get('title')
  };

  run('directories:create', () => api('/directories', { method: 'POST', body: JSON.stringify(payload) }));
});

document.querySelector('[data-action="load-tickets"]').addEventListener('click', () => {
  const page = document.getElementById('tickets-page').value || '0';
  run('moderation/all', () => api(`/moderation/all?page=${page}`));
});

document.getElementById('moderate-form').addEventListener('submit', (event) => {
  event.preventDefault();
  const form = new FormData(event.target);
  const payload = {
    ticketId: Number(form.get('ticketId')),
    result: form.get('result')
  };

  run('moderation:apply', () => api('/moderation', { method: 'POST', body: JSON.stringify(payload) }));
});

document.getElementById('add-type-form').addEventListener('submit', (event) => {
  event.preventDefault();
  const form = new FormData(event.target);
  run('admin:type', () => api('/admin/types', {
    method: 'POST',
    headers: { 'Content-Type': 'text/plain' },
    body: form.get('type')
  }));
});

document.getElementById('permit-form').addEventListener('submit', (event) => {
  event.preventDefault();
  const form = new FormData(event.target);
  const payload = {
    username: form.get('username'),
    directoryId: Number(form.get('directoryId')),
    permitType: form.get('permitType')
  };

  run('admin:permit:create', () => api('/admin/permits', { method: 'POST', body: JSON.stringify(payload) }));
});

document.querySelector('[data-action="load-permits"]').addEventListener('click', () => {
  run('admin:permits', () => api('/admin/permits'));
});
