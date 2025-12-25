import { execSync } from 'child_process';
import fs from 'fs';

const API_URL = 'http://localhost:8080/v3/api-docs';
const OUTPUT_DIR = './docs';
const OPENAPI_FILE = `${OUTPUT_DIR}/openapi.json`;
const HTML_FILE = `${OUTPUT_DIR}/api-docs.html`;

if (!fs.existsSync(OUTPUT_DIR)) {
  fs.mkdirSync(OUTPUT_DIR);
}

console.log('📥 Fetching OpenAPI spec...');
execSync(`npx @redocly/cli bundle ${API_URL} -o ${OPENAPI_FILE}`, {
  stdio: 'inherit',
});

console.log('📄 Generating HTML documentation...');
execSync(`npx @redocly/cli build-docs ${OPENAPI_FILE} -o ${HTML_FILE}`, {
  stdio: 'inherit',
});

console.log('✅ API documentation generated:');
console.log(`   ${HTML_FILE}`);
