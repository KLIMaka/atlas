this:setSize(this:parent():width() / 5, this:parent():height());

handlers = {};
function HANDLE(event)
    handler = handlers[event];
    if handler ~= nil then
        handler();
    end
end

lib = dofile("scripts/mylib.lua");
dragControl = lib.createDragControl(handlers, engine, this);

handlers.mouseMove = function()

    if dragControl.enabled then
        local maxx = this:parent():width() - this:width();
        local nx = dragControl:x();
        if nx < 0 then nx = 0 end
        if nx > maxx then nx = maxx end 
        this:setPos(nx, this:worldPos()[2]);
    end
end
