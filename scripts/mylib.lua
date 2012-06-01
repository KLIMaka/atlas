
local lib = {};

lib.decorateBefore = function(func, decor)
    if funct == nil then
        return decor;
    else
        return function()
            decor();
            func();
        end
    end
end

lib.getScript = function(this)

    local i = function(t,k)
        return function(...)
            return this:call(k, arg);
        end
    end
    local script = {};
    setmetatable(script, {__index = i});
    return script;
end

lib.createDragControl = function(h, engine, this)

    local control = {
        enabled = false,
        xoff = 0,
        yoff = 0,
        x = function(t) return engine.mouse.x - t.xoff end,
        y = function(t) return engine.mouse.y - t.yoff end,
    };

    h.mouseDown = lib.decorateBefore(h.mouseDown, function()
        control.enabled = true;
        control.xoff = engine.mouse.x - this:worldPos()[1];
        control.yoff = engine.mouse.y - this:worldPos()[2];
    end);

    h.mouseExit = lib.decorateBefore(h.mouseExit, function()
        control.enabled = false;
    end);

    h.mouseUp = lib.decorateBefore(h.mouseUp, function()
        control.enabled = false;
    end);

    return control;
end

return lib;
