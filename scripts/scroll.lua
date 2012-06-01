slider = engine.ui:createPanel("data/panel/panel2.png", 0.4);
this:addChild(slider);
slider:setScript(engine.scripts:create("scripts/scroll.slider.lua"));
this:setMulColor(1.2, 1.2, 1.2, 1);

handlers = {};
function HANDLE(event)
    handler = handlers[event];
    if handler ~= nil then
        handler();
    end
end


lib = dofile("scripts/mylib.lua");
ss = lib.getScript(slider);
