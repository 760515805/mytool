 var base_code = '';
    var jsdecoder;
    var jscolorizer;
    var code = '';
    var time = 0;
    function emptyMsg()
    {
    	document.getElementById('a1').value="";
    }
    function decode()
    {
        code = '';
        base_code = '';
        jsdecoder = new JsDecoder();
        jscolorizer = new JsColorizer();

        if ($('msg').innerHTML.length) {
            do_clean_init();
        } else {
            jsdecoder.s = $("a1").value;
            do_decode_init();
        }
    }
    function do_decode_init()
    {
        $('msg').innerHTML += 'Decoding .. ';
        setTimeout(do_decode, 50);
    }
    function do_decode()
    {
        time = time_start();
        try {
            code = jsdecoder.decode();
            base_code = code;
        } catch (e) {
            $('msg').innerHTML += 'error<br><br>'+new String(e).replace(/\n/g, '<br>');
            return;
        }
        $('msg').innerHTML += 'ok ('+time_end(time)+' sec)<br>';
        setTimeout(do_colorize_init, 50);
    }
    function do_colorize_init()
    {
        $('msg').innerHTML += 'Colorizing .. ';
        setTimeout(do_colorize, 50);
    }
    function do_colorize()
    {
        time = time_start();
        code = code.replace(/&/g, "&amp;");
        code = code.replace(/</g, "&lt;");
        code = code.replace(/>/g, "&gt;");
        jscolorizer.s = code;
        try {
            code = jscolorizer.colorize();
        } catch (e) {
            $('msg').innerHTML += 'error<br><br>'+new String(e).replace(/\n/g, '<br>');
            return;
        }
        $('msg').innerHTML += 'ok ('+time_end(time)+' sec)<br>';
        /* debug:
        $('msg').innerHTML += '&nbsp;&nbsp;&nbsp;&nbsp;'+jscolorizer.showTimes().replace(/\n$/, '').replace(/\n/g, '<br>&nbsp;&nbsp;&nbsp;&nbsp;')+'<br>';
        */
        setTimeout(do_insert_init, 50);
    }
    function do_insert_init()
    {
        $('msg').innerHTML += 'Inserting code .. ';
        setTimeout(do_insert, 50);
    }
    function do_insert()
    {
        time = time_start();
        try {
        
            code = new String(code);
            code = code.replace(/(\r\n|\r|\n)/g, "<br>\n");
            code = code.replace(/<font\s+/gi, '<font@@@@@');
            code = code.replace(/( |\t)/g, '&nbsp;');
            code = code.replace(/<font@@@@@/gi, '<font ');

            code = code.replace(/\n$/, '');

            var count = 0;
            var pos = code.indexOf("\n");
            while (pos != -1) {
               count++;
               pos = code.indexOf("\n", pos+1);
            }
            count++;

            pad = new String(count).length;
            var lines = '';

            for (var i = 0; i < count; i++) {
                var p = pad - new String(i+1).length;
                var no = new String(i+1);
                for (k = 0; k < p; k++) { no = '&nbsp;'+no; }
                no += '&nbsp;';
                lines += '<div style="background: #fff; color: #666;">'+no+'</div>';
            }
            $('lines').innerHTML = lines;

            $('code_area').style.display = 'block';
            $('sel_all').style.display = 'block';
            $("a2").innerHTML = code;

        } catch (e) {
            $('msg').innerHTML += 'error<br><br>'+new String(e).replace(/\n/g, '<br>');
            return;
        }
        
        $('msg').innerHTML += 'ok ('+time_end(time)+' sec)';
        code = '';
    }
    function do_clean_init()
    {
        //$('msg').innerHTML = 'Removing code .. ';
        //setTimeout(do_clean, 50);

        $('msg').innerHTML = '';
        do_clean();
    }
    function do_clean()
    {
        time = time_start();
        //$('lines').innerHTML = '';
        //$('a2').innerHTML = '';
        $('code_area').style.display = 'none';
        base_code = '';
        $('sel_all').style.display = 'none';
        //$('insert_area').value = '';
        $('insert_div').style.display = 'none';
        jsdecoder.s = $("a1").value;
        
        //$('msg').innerHTML += 'ok ('+time_end(time)+' sec)<br>';
        //setTimeout(do_decode_init, 50);

        do_decode_init();
    }
    function insert_textarea()
    {
        $('insert_div').style.display = 'block';
        $('insert_area').value = base_code;
        $('insert_area').focus();
        $('insert_area').select();
    }
    function $(id)
    {
        return document.getElementById(id);
    }
    function time_micro()
    {
        var micro = new String(new Date().getTime());
        micro = micro.substr(0, micro.length-3) + '.' + micro.substr(micro.length-3, 3);
        return parseFloat(micro);
    }
    function time_start()
    {
        return time_micro();
    }
    function time_get(start)
    {
        return time_micro() - start;
    }
    function time_end(start)
    {
        return time_round(time_micro() - start);
    }
    function time_round(time)
    {
        time = Math.round(time * 100) / 100;
        if (time === 0) { time = 0.01; }
        return time;
    }