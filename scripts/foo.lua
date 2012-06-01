pan = engine.ui:createPanel("data/panel/panel2.png", 0.4);
this:addChild(pan);
pan:setScript(engine.scripts:create("scripts/grip.lua"));

cont = engine.ui:createPanel("data/panel/panel2.png", 0.4);
this:addChild(cont);
cont:setPos(10, 10);
cont:setSize(this:width() - 20, 100);
cont:setScript(engine.scripts:create("scripts/scrollable.lua"));

handlers = {};
function HANDLE(event)
    local handler = handlers[event];
    if handler ~= nil then
        handler();
    end
end


lib = dofile("scripts/mylib.lua");

dragger = lib.createDragControl(handlers, engine, this);
handlers.mouseMove = function()

    if dragger.enabled then
        this:setPos(dragger:x(), dragger:y());
    end
end

handlers.onResize = function()
    cont:setSize(this:width() - 20, cont:height());
end

