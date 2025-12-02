INSERT INTO notice (id, version, filename, bytes, updated_at, status, type)
VALUES (
  'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
  0,
  'ContratoES.pdf',
  0,
  NOW(),
  'UNKNOWN_TYPE',
  'UNKNOWN'
) ON CONFLICT (id) DO NOTHING;