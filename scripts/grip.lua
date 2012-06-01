this:setMulColor(1,1,1,0.5);
this:setSize(20, 20);
this:setPos(this:parent():width() - 20, this:parent():height() - 20);

handlers = {};
function HANDLE(event)
    handler = handlers[event];
    if handler ~= nil then
        handler();
    end
end

lib = dofile("scripts/mylib.lua");

dragger = lib.createDragControl(handlers, engine, this);

handlers.mouseMove = function()
    
    if dragger.enabled then
        this:parent():setSize(dragger:x() + 20, dragger:y() + 20);
        this:setPos(this:parent():width() - 20, this:parent():height() - 20);
    end
end

