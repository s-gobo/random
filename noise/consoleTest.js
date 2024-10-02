async function run() {
  let i = 0;
  while(true) {
    console.clear();
    console.log(i);
    i++;
    await new Promise(resolve => setTimeout(resolve, 1000));
  }
}
run();
