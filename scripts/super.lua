sys = luajava.bindClass("java.lang.System");
array = luajava.bindClass("java.lang.reflect.Array");

function println(msg)
    sys.out:println(msg);
end

function x() return this:worldPos()[1] end
function y() return this:worldPos()[2] end
function w() return this:width() end
function h() return this:height() end

function mouseEnter()
end

function mouseExit()
end

function mouseClick()
end

function mouseDown()
end

function mouseUp()
end

function mouseMove()
end

