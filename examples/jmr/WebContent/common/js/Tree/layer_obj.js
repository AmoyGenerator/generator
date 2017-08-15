/********************************************************************
* layer_obj.js                                                      *
*                                                                   * 
* These functions allow dynamic HTML effects to be added to any     *
* web page and will work on both Netscape Communicator 4.0+ and MS  *
* Internet Explorer 4.0+ browsers.                                  *
*                                                                   *
********************************************************************/

var layerList = new Array();


function createLayer(name, left, top, width, height, visible, content) {

  var z = layerList.length;
  var layer;

  layerList[z] = name;

  if (document.layers) {
    document.writeln('<layer name="' + name + '" left=' + left + ' top=' + top + ' width=' + width + ' height=' + height +  ' visibility=' + (visible ? '"show"' : '"hide"') + ' z-index=' + z + '>');
    document.writeln(content);
    document.writeln('</layer>');
    layer = getLayer(name);
    layer.width = width;
    layer.height = height;
  }

  if (document.all) {
    document.writeln('<div id="' + name + '" style="position:absolute; overflow:none; left:' + left + 'px; top:' + top + 'px; width:' + width + 'px; height:' + height + 'px;' + ' visibility:' + (visible ? 'visible;' : 'hidden;') + ' z-index:' + z + '">');
    document.writeln(content);
    document.writeln('</div>');
  }

  clipLayer(name, 0, 0, width, height);
}

function createLayerM(name, left, top, width, height, visible, content, mOver, mOut) {

  var z = layerList.length;
  var layer;

  layerList[z] = name;

  if (document.layers) {
    document.writeln('<layer name="' + name + '" onMouseOver="' + mOver + ';" onMouseOut="' + mOut + ';" left=' + left + ' top=' + top + ' width=' + width + ' height=' + height +  ' visibility=' + (visible ? '"show"' : '"hide"') + ' z-index=' + z + '>');
    document.writeln(content);
    document.writeln('</layer>');
    layer = getLayer(name);
    layer.width = width;
    layer.height = height;
  }

  if (document.all) {
    document.writeln('<div id="' + name + '" onMouseOver="' + mOver + ';" onMouseOut="' + mOut + ';" style="position:absolute; overflow:none; left:' + left + 'px; top:' + top + 'px; width:' + width + 'px; height:' + height + 'px;' + ' visibility:' + (visible ? 'visible;' : 'hidden;') + ' z-index:' + z + '">');
    document.writeln(content);
    document.writeln('</div>');
  }

  clipLayer(name, 0, 0, width, height);
}

function hideLayer(name) {

  var layer = getLayer(name);

  if (document.layers)
    layer.visibility = "hide";
  if (document.all)
    layer.visibility = "hidden";
}

function showLayer(name) {

  var layer = getLayer(name);

  if (document.layers)
    layer.visibility = "show";
  if (document.all)
    layer.visibility = "visible";
}

function isVisible(name) {

  var layer = getLayer(name);

  if (document.layers && layer.visibility == "show")
    return(true);
  if (document.all && layer.visibility == "visible")
    return(true);
  return(false);
}

function moveLayer(name, x, y) {

  var layer = getLayer(name);

  if (document.layers)
    layer.moveTo(x, y);
  if (document.all) {
    layer.left = x;
    layer.top  = y;
  }
}

// ====== 1998.11.11 ======
function moveByLayer(name, x, y) {

  var layer = getLayer(name);

  if (document.layers)
    layer.moveBy(x, y);
  if (document.all) {
    layer.left = x + parseInt(layer.left);
    layer.top  = y + parseInt(layer.top);
  }
}


var animRate = 25;    // Basically sets the update rate for animations.

function slideLayer(name, x, y, speed, code) {

  var layer = getLayer(name);

  // If the layer is currently being moved, cancel it.
 
  if (layer.slideID && layer.slideID != null)
    clearTimeout(layer.slideID);

  // Fix up all parameters.

  if (!code)
    code = "";

  // If any parameter other than 'name' is not an Array, make it one.

  x     = makeArray(x);
  y     = makeArray(y);
  speed = makeArray(speed);
  code  = makeArray(code);

  // Pad all arrays to the same length.

  var max = Math.max(x.length, Math.max(y.length, Math.max(speed.length, code.length)));
  while (x.length < max)
    x[x.length] = x[x.length - 1];
  while (y.length < max)
    y[y.length] = y[y.length - 1];
  while (speed.length < max)
    speed[speed.length] = speed[speed.length - 1];
  while (code.length < max)
    code[code.length] = code[code.length - 1];

  // Call the slide function with array parameters.

  goSlide(name, x, y, speed, code);
}

function goSlide(name, x, y, speed, code) {

  var layer = getLayer(name);
  var hrzn, vert, left, top, steps;

  if (document.layers) {
    hrzn = x[0] - layer.left;
    vert = y[0] - layer.top;
    left = layer.left;
    top  = layer.top;
  }

  if (document.all) {
    hrzn = x[0] - layer.pixelLeft;
    vert = y[0] - layer.pixelTop;
    left = layer.pixelLeft;
    top  = layer.pixelTop;
  }

  // Calculate how many steps it will take and the size of each step.

  steps = (Math.max(Math.abs(hrzn), Math.abs(vert)) / speed[0]) * (1000 / animRate);

  if (steps <= 0)
    return;

  // Save remaining array values.

  layer.slideX     = x.slice(1, x.length);
  layer.slideY     = y.slice(1, y.length);
  layer.slideSpeed = speed.slice(1, speed.length);
  layer.slideCode  = code.slice(1, code.length);

  // Set up movement values.

  layer.slideLeft   = left;
  layer.slideTop    = top;
  layer.slideDx     = hrzn / steps;
  layer.slideDy     = vert / steps;
  layer.slideFinalX = x[0];
  layer.slideFinalY = y[0];
  layer.slideSteps  = Math.floor(steps);
  layer.slideAction = code[0];

  // Start the slide.

  slideStep(name);
}

function slideStep(name) {

  // Moves the layer one step.

  var layer = getLayer(name);

  layer.slideLeft += layer.slideDx;
  layer.slideTop  += layer.slideDy;
  if (document.layers)
    layer.moveTo(layer.slideLeft, layer.slideTop);
  if (document.all) {
    layer.left = layer.slideLeft;
    layer.top  = layer.slideTop;
  }

  // If more steps remain, call this function again.

  if (layer.slideSteps-- > 0)
    layer.slideID = setTimeout('slideStep("' + name + '")', animRate);

  // Otherwise, move layer to final position, execute the any code and look for more slide parameters.

  else {
   if (document.layers)
      layer.moveTo(layer.slideFinalX, layer.slideFinalY);
    if (document.all) {
      layer.left = layer.slideFinalX;
      layer.top  = layer.slideFinalY;
    }

    // Execute code, if any.

    layer.slideID = null;
    if (layer.slideAction != "")
      eval(layer.slideAction);

    // If any more slide parameters are left, start a new slide.

    if (layer.slideX.length > 0)
      goSlide(name, layer.slideX, layer.slideY, layer.slideSpeed, layer.slideCode);
    else
      layer.slideID = null;
  }
}

function clipLayer(name, clipleft, cliptop, clipright, clipbottom) {

  var layer = getLayer(name);

  if (document.layers) {
    layer.clip.left   = clipleft;
    layer.clip.top    = cliptop;
    layer.clip.right  = clipright;
    layer.clip.bottom = clipbottom;
  }
  if (document.all)
    layer.clip = 'rect(' + cliptop + ' ' +  clipright + ' ' + clipbottom + ' ' + clipleft +')';
}

function swipeLayer(name, clipleft, cliptop, clipright, clipbottom, speed, code) {

  var layer = getLayer(name);

  // If the layer is currently being clipped, cancel it.
 
  if (layer.swipeID && layer.swipeID != null)
    clearTimeout(layer.swipeID);

  // Fix up all parameters.

  if (!code)
    code = "";

  // If any parameter other than 'name' is not an Array, make it one.

  clipleft   = makeArray(clipleft);
  cliptop    = makeArray(cliptop);
  clipright  = makeArray(clipright);
  clipbottom = makeArray(clipbottom);
  speed      = makeArray(speed);
  code       = makeArray(code);

  // Pad all arrays to the same length.

  var max = Math.max(clipleft.length, Math.max(clipright.length, Math.max(cliptop.length, Math.max(clipbottom.length, Math.max(speed.length, code.length)))));
  while (clipleft.length < max)
    clipleft[clipleft.length] = clipleft[clipleft.length - 1];
  while (clipright.length < max)
    clipright[clipright.length] = clipright[clipright.length - 1];
  while (cliptop.length < max)
    cliptop[cliptop.length] = cliptop[cliptop.length - 1];
  while (clipbottom.length < max)
    clipbottom[clipbottom.length] = clipbottom[clipbottom.length - 1];
  while (speed.length < max)
    speed[speed.length] = speed[speed.length - 1];
  while (code.length < max)
    code[code.length] = code[code.length - 1];

  // Call the swipe function with array parameters.

  goSwipe(name, clipleft, cliptop, clipright, clipbottom, speed, code)
}

function goSwipe(name, clipleft, cliptop, clipright, clipbottom, speed, code) {

  var layer = getLayer(name);

  var hrzn1, vert1;
  var hrzn2, vert2;
  var max1, max2;
  var steps;

  if (document.layers) {
    hrzn1 = clipleft[0] - layer.clip.left;
    vert1 = cliptop[0] - layer.clip.top;
    hrzn2 = clipright[0] - layer.clip.right;
    vert2 = clipbottom[0] - layer.clip.bottom;
  }
  if (document.all) {
    if (!layer.clip)
      clipLayer(name, 0, 0, layer.pixelWidth, layer.pixelHeight);
    var clip = getClipValues(layer.clip);
    hrzn1 = clipleft[0] - clip[3];
    vert1 = cliptop[0] - clip[0];
    hrzn2 = clipright[0] - clip[1];
    vert2 = clipbottom[0] - clip[2];
  }
  max1 = Math.max(Math.abs(hrzn1), Math.abs(vert1));
  max2 = Math.max(Math.abs(hrzn2), Math.abs(vert2));
  steps = (Math.max(max1, max2) / speed[0]) * (1000 / animRate);

  if (steps <= 0)
    return;

  // Save remaining array values.

  layer.swipeClipleft   = clipleft.slice(1, clipleft.length);
  layer.swipeCliptop    = cliptop.slice(1, cliptop.length);
  layer.swipeClipright  = clipright.slice(1, clipright.length);
  layer.swipeClipbottom = clipbottom.slice(1, clipbottom.length);
  layer.swipeSpeed      = speed.slice(1, speed.length);
  layer.swipeCode       = code.slice(1, code.length);

  // Set up clipping values.

  if (document.layers) {
    layer.swipeLeft   = layer.clip.left;
    layer.swipeTop    = layer.clip.top;
    layer.swipeRight  = layer.clip.right;
    layer.swipeBottom = layer.clip.bottom;
  }
  if (document.all) {
    layer.swipeLeft   = parseInt(clip[3], 10);
    layer.swipeTop    = parseInt(clip[0], 10);
    layer.swipeRight  = parseInt(clip[1], 10);
    layer.swipeBottom = parseInt(clip[2], 10);
  }
  layer.swipeDx1         = hrzn1 / steps;
  layer.swipeDy1         = vert1 / steps;
  layer.swipeDx2         = hrzn2 / steps;
  layer.swipeDy2         = vert2 / steps;
  layer.swipeFinalLeft   = clipleft[0];
  layer.swipeFinalTop    = cliptop[0];
  layer.swipeFinalRight  = clipright[0];
  layer.swipeFinalBottom = clipbottom[0];
  layer.swipeSteps       = Math.floor(steps);
  layer.swipeAction      = code[0];

  // Start the swipe.

  swipeStep(name);
}

function swipeStep(name) {

  var layer = getLayer(name);

  // Adjust the layer's clipping area by one step.

  layer.swipeLeft   += layer.swipeDx1;
  layer.swipeTop    += layer.swipeDy1;
  layer.swipeRight  += layer.swipeDx2;
  layer.swipeBottom += layer.swipeDy2;

  if (document.layers) {
    layer.clip.left   = layer.swipeLeft;
    layer.clip.top    = layer.swipeTop;
    layer.clip.right  = layer.swipeRight;
    layer.clip.bottom = layer.swipeBottom;
  }
  if (document.all)
    layer.clip = 'rect(' + layer.swipeTop + ' ' + layer.swipeRight + ' ' + layer.swipeBottom + ' ' + layer.swipeLeft +')';

  // If more steps remain, call this function again.

  if (--layer.swipeSteps > 0)
    layer.swipeID = setTimeout('swipeStep("' + name + '")', animRate);

  // Otherwise, set layer's final clip area, execute the any code and look for more swipe parameters.

  else {
    if (document.layers) {
      layer.clip.left   = Math.round(layer.swipeFinalLeft);
      layer.clip.top    = Math.round(layer.swipeFinalTop);
      layer.clip.right  = Math.round(layer.swipeFinalRight);
      layer.clip.bottom = Math.round(layer.swipeFinalBottom);
    }
    if (document.all)
      layer.clip = 'rect(' + layer.swipeFinalTop + ' ' + layer.swipeFinalRight + ' ' + layer.swipeFinalBottom + ' ' + layer.swipeFinalLeft +')';

    // Execute code, if any.

    if (layer.swipeAction != "")
      eval(layer.swipeAction);

    // If any more swipe parameters are left, start a new swipe.

    if (layer.swipeClipleft.length > 0)
      goSwipe(name, layer.swipeClipleft, layer.swipeCliptop, layer.swipeClipright, layer.swipeClipbottom, layer.swipeSpeed, layer.swipeCode);
    else
      layer.swipeID = null;
  }
}

function scrollLayer(name, dx, dy) {

  var cl = getClipLeft(name);
  var ct = getClipTop(name);
  var cr = getClipRight(name);
  var cb = getClipBottom(name);
  var l  = getLeft(name);
  var t  = getTop(name);

  // If scrolling the given amounts would move past the edges of the layer,
  // adjust the values so we stop right at the edge.

  if (cl + dx < 0)
    dx = -cl;
  else if (cr + dx > getWidth(name))
    dx = getWidth(name) - cr;
  if (ct + dy < 0)
    dy = -ct;
  else if (cb + dy > getHeight(name))
    dy = getHeight(name) - cb;

  // Move both the clipping region and the layer so that the contents move
  // but the viewable region of the layer appears fixed relative to the page.

  clipLayer(name, cl + dx, ct + dy, cr + dx, cb + dy);
  moveLayer(name, l - dx, t - dy);
}


function setBgColor(name, color) {

  var layer = getLayer(name);

  if (document.layers)
    layer.bgColor = color;
  else if (document.all)
    layer.backgroundColor = color;
}

function setBgImage(name, imagesrc) {

  var layer = getLayer(name);

  if (document.layers)
    layer.background.src = imagesrc;
  else if (document.all)
    layer.backgroundImage = "url(" + imagesrc + ")";
}

function replaceContent(name, content) {

  if (document.layers) {
    var layer = getLayer(name);
    layer.document.open();
    layer.document.writeln(content);
    layer.document.close();
  }
  else if (document.all) {
    var str = "document.all." + name + ".innerHTML = '" + content + "'";
    eval(str);
  }
}

function getLeft(name) {

  var layer = getLayer(name);

  if (document.layers)
    return(layer.left);
  else if (document.all)
    return(layer.pixelLeft);
  else
    return(null);
}

function getTop(name) {

  var layer = getLayer(name);

  if (document.layers)
    return(layer.top);
  else if (document.all)
    return(layer.pixelTop);
  else
    return(null);
}

function getRight(name) {

  var layer = getLayer(name);

  if (document.layers)
    return(layer.left + layer.width);
  else if (document.all)
    return(layer.pixelLeft + layer.pixelWidth);
  else
    return(null);
}

function getBottom(name) {

  var layer = getLayer(name);

  if (document.layers)
    return(layer.top + layer.height);
  else if (document.all)
    return(layer.pixelTop + layer.pixelHeight);
  else
    return(null);
}

function getWidth(name) {

  var layer = getLayer(name);

  if (document.layers)
    return(layer.width);
  else if (document.all)
    return(layer.pixelWidth);
  else
    return(null)
}

function getHeight(name) {

  var layer = getLayer(name);

  if (document.layers)
    return(layer.height);
  else if (document.all)
    return(layer.pixelHeight);
  else
    return(null);
}

function getClipLeft(name) {

  var layer = getLayer(name);

  if (document.layers)
    return(layer.clip.left);
  else if (document.all) {
    var str =  layer.clip;
    if (!str)
      return(0);
    var clip = getClipValues(layer.clip);
    return(clip[3]);
  }
  else
    return(null);
}

function getClipTop(name) {

  var layer = getLayer(name);

  if (document.layers)
    return(layer.clip.top);
  else if (document.all) {
    var str =  layer.clip;
    if (!str)
      return(0);
    var clip = getClipValues(layer.clip);
    return(clip[0]);
  }
  else
    return(null);
}

function getClipRight(name) {

  var layer = getLayer(name);

  if (document.layers)
    return(layer.clip.right);
  else if (document.all) {
    var str =  layer.clip;
    if (!str)
      return(layer.pixelWidth);
    var clip = getClipValues(layer.clip);
    return(clip[1]);
  }
  else
    return(null);
}

function getClipBottom(name) {

  var layer = getLayer(name);

  if (document.layers)
    return(layer.clip.bottom);
  else if (document.all) {
    var str =  layer.clip;
    if (!str)
      return(layer.pixelHeight);
    var clip = getClipValues(layer.clip);
    return(clip[2]);
  }
  else
    return(null);
}

function getClipWidth(name) {

  var layer = getLayer(name);

  if (document.layers)
    return(layer.clip.width);
  else if (document.all) {
    var str =  layer.clip;
    if (!str)
      return(layer.pixelWidth);
    var clip = getClipValues(layer.clip);
    return(clip[1] - clip[3]);
  }
  else
    return(null);
}

function getClipHeight(name) {

  var layer = getLayer(name);

  if (document.layers)
    return(layer.clip.height);
  else if (document.all) {
    var str =  layer.clip;
    if (!str)
      return(layer.pixelHeight);
    var clip = getClipValues(layer.clip);
    return(clip[2] - clip[0]);
  }
  else
    return(null);
}

function getWinWidth() {

  if (document.layers)
    return(window.innerWidth);
  else if (document.all)
    return(document.body.clientWidth);
  else
    return(null);
}

function getWinHeight() {

  if (document.layers)
    return(window.innerHeight);
  else if (document.all)
    return(document.body.clientHeight);
  else
    return(null);
}

function getzIndex(name) {

  var layer = getLayer(name);

  if (document.layers)
    return(layer.zIndex);
  else if (document.all)
    return(layer.zIndex);
  else
    return(null);
}

function setzIndex(name, z) {

  var layer = getLayer(name);

  if (document.layers)
    layer.zIndex = z;
  if (document.all)
    layer.zIndex = z;
}

function bringToFront(name) {

  var i, temp;

  layerList.sort(sortzIndex);

  i = layerList.length - 1;
  temp = getzIndex(layerList[i]);
  while (i > 0 && layerList[i] != name) {
    setzIndex(layerList[i], getzIndex(layerList[i - 1]));
    i--;
  }
  setzIndex(name, temp);
}

function sendToBack(name) {

  var i, temp;

  layerList.sort(sortzIndex);
  i = 0;
  temp = getzIndex(layerList[i]);
  while (i < layerList.length - 2 && layerList[i] != name) {
    setzIndex(layerList[i], getzIndex(layerList[i + 1]));
    i++;
  }
  setzIndex(name, temp);
}

function sortzIndex(a, b) {

  return(getzIndex(a) - getzIndex(b));
}

function getImgSrc(imagename) {

  var i, layer;

  // If the image exists in the document object, return the source.

  if (document.images[imagename])
    return document.images[imagename].src;

  // Otherwise, for Netscape, search through the layers for the named image.

  else if (document.layers)
    for (i = 0; i < layerList.length; i++) {
      layer = getLayer(layerList[i]);
      if (layer.document.images[imagename])
        return layer.document.images[imagename].src;
    }

  return(null);
}

function setImgSrc(imagename, imagesrc) {

  var i, layer;

  // If the image exists in the document object, change the source.

  if (document.images[imagename]) {
    document.images[imagename].src = imagesrc;
    return;
  }

  // Otherwise, for Netscape, search through the layers for the named image.

  else if (document.layers) {
    var found = false;
    for (i = 0; i < layerList.length && !found; i++) {
      layer = getLayer(layerList[i]);
      if (layer.document.images[imagename]) {
        layer.document.images[imagename].src = imagesrc;
        found = true;
      }
    }
  }
}

function getClipValues(str) {

  var clip = new Array();
  var i;

  // Parse out the clipping values for IE layers.

  i = str.indexOf("(");
  clip[0] = parseInt(str.substring(i + 1, str.length), 10);
  i = str.indexOf(" ", i + 1);
  clip[1] = parseInt(str.substring(i + 1, str.length), 10);
  i = str.indexOf(" ", i + 1);
  clip[2] = parseInt(str.substring(i + 1, str.length), 10);
  i = str.indexOf(" ", i + 1);
  clip[3] = parseInt(str.substring(i + 1, str.length), 10);
  return(clip);
}

function getLayer(name) {

  // Returns a handle to the named layer.

  if (document.layers)
    return(document.layers[name]);
  else if (document.all) {
    layer = eval('document.all.' + name + '.style');
    return(layer);
  }
  else
    return(null);
}

function makeArray(a) {

  var temp;

  if (!a.join) {
    temp = a;
    a = new Array();
    a[0] = temp;
  }
  return a;
}