handlers = {};
function HANDLE(event)
    handler = handlers[event];
    if handler ~= nil then
        handler();
    end
end

lib = dofile("scripts/mylib.lua");

maxx = this:width();
maxy = this:height();
curx = 0;
cury = 0;
childs = {};

function addPanel(panel)

    local pw = panel:width();
    local ph = panel:height();
    local px = panel:worldPos()[1];
    local py = panel:worldPos()[2];

    if px + pw > maxx then maxx = px + pw end
    if py + ph > maxy then maxy = py + ph end
    
    panel:setPos(px - curx, py - cury);
    table.insert(childs, panel);
    this:addChild(panel);
end

hscroll = engine.ui:createPanel("data/panel/panel2.png", 0.4);
this:addChild(hscroll);
hscroll:setSize(this:width(), 20);
hscroll:setPos(0, this:height() - 20);
hscroll:setScript(engine.scripts:create("scripts/scroll.lua"));

handlers.onResize = function()
    hscroll:setSize(this:width(), hscroll:height());
end

handlers.Layout = function()
    
    for ch in childs do
        
    end
end

