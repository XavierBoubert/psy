const puppeteer = require('puppeteer');
const path = require('path');

(async () => {
  const browser = await puppeteer.launch({ headless: true });
  const page = await browser.newPage();
  await page.setViewport({ width: 412, height: 915 });
  const errors = [];
  page.on('pageerror', (e) => errors.push(String(e)));
  page.on('console', (m) => { if (m.type() === 'error') errors.push(m.text()); });

  const target = 'file://' + path.resolve('companion/ressources/maquette/index.html').replace(/\\/g, '/');
  await page.goto(target);
  await new Promise((r) => setTimeout(r, 400));
  await page.screenshot({ path: 'companion/ressources/maquette/_verif-therapie.png' });

  await page.click('#btn-nuit');
  await new Promise((r) => setTimeout(r, 500));
  await page.screenshot({ path: 'companion/ressources/maquette/_verif-therapie-nuit.png' });
  await page.click('#btn-nuit');

  await page.click('[data-ouvre="ppc-origine-fuite"]');
  await new Promise((r) => setTimeout(r, 400));
  await page.screenshot({ path: 'companion/ressources/maquette/_verif-panneau.png' });
  await page.click('[data-ferme]');
  await new Promise((r) => setTimeout(r, 400));

  await page.click('[data-aller="0,0"]');
  await new Promise((r) => setTimeout(r, 600));
  await page.screenshot({ path: 'companion/ressources/maquette/_verif-centre.png' });

  console.log('erreurs JS:', errors.length ? errors : 'aucune');
  await browser.close();
})();
