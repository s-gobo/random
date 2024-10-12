let randomNdimSpherePoint = function(dim) {
  //get a random point in a unit N-dimentional sphere
  let dist, components;
  do {
    components = [];
    dist = 0;
    for (let i = 0; i < dim; i++){
      let noise = 2 * Math.random() - 1;
      components.push(noise);
      dist += noise ** 2;
    }
  } while (dist > 1);
  dist = Math.sqrt(dist);
  //move the point away from the center
  components = components.map(x => x / dist);
  return components;
}

point = randomNdimSpherePoint(19);
console.log(point);
//verify is on the surface
console.log(point.reduce((sum, x) => sum + x ** 2, 0));
