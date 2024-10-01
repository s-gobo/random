let randomSpherePoint = function() {
  let x, y, z, dist;
  //get a random point in a unit sphere
  do {
    x = 2 * Math.random() - 1;
    y = 2 * Math.random() - 1;
    z = 2 * Math.random() - 1;
    dist = Math.sqrt(x ** 2 + y ** 2 + z ** 2);
  } while (dist > 1);
  //move the point away from the center
  x /= dist;
  y /= dist;
  z /= dist;
  
  return {x: x, y: y, z: z};
}

point = randomSpherePoint();
console.log(point);
//verify is on the surface
console.log(Math.sqrt(point.x ** 2 + point.y ** 2 + point.z ** 2));
