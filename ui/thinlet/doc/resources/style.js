/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
function replacePage(fid) {
	c = document.getElementById('content');
	l = window.parent.document.getElementById(fid);
	if (l != null) {
		l.innerHTML = c.innerHTML;
		c.innerHTML = '';
	}
}

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
function tabbed() {
	bar = document.getElementById('tabbar');
	bar.className = 'tabbar';
	tabs = bar.childNodes;
	pgs = bar.nextSibling; pages = pgs.childNodes;
	n = getindex(pgs, pages, document.URL);
	for (i = 0; i < tabs.length; i++) {
		tabs[i].className = (i == n) ? 'tabsel' : 'tab';
		pages[i].className = (i == n) ? 'pgsel' : 'pg';
		addlistener(tabs[i], 'mouseover', over);
		addlistener(tabs[i], 'mouseout', out);
		addlistener(tabs[i], 'mousedown', down);
	}
}

function over(event) { changeclass(event, 'tab', 'tabhov'); }

function out(event) { changeclass(event, 'tabhov', 'tab'); }

function down(event) {
	t = gettarget(event);
	tabs = t.parentNode.childNodes;
	pages = t.parentNode.nextSibling.childNodes;
	for (i = 0; i < tabs.length; i++) {
		sel = (tabs[i] == t);
		tabs[i].className = sel ? 'tabsel' : 'tab';
		pages[i].className = sel ? 'pgsel' : 'pg';
	}
}

function changeclass(e, prev, curr) {
	t = gettarget(e);
	if (t.className == prev) t.className = curr;
}

function addlistener(target, type, listener) {
	if (target.addEventListener) target.addEventListener(type, listener, false);
	else target.attachEvent('on' + type, listener);
}

function gettarget(e) {
	return (e.target) ? e.target : e.srcElement;
}

function settab(a) {
	bar = document.getElementById('tabbar');
	tabs = bar.childNodes;
	pgs = bar.nextSibling; pages = pgs.childNodes;
	n = getindex(pgs, pages, a.href);
	for (i = 0; i < tabs.length; i++) {
		tabs[i].className = (i == n) ? 'tabsel' : 'tab';
		pages[i].className = (i == n) ? 'pgsel' : 'pg';
	}
}

function getindex(pgs, pages, url) {
	if ((i = url.indexOf('#')) > -1) {
		ls = document.getElementsByName(url.substr(i + 1));
		if (ls.length > 0) for (lnk = ls[0]; (prn = lnk.parentNode) != null; lnk = prn) {
			if (prn == pgs) for (j = 0; j < pages.length; j++) if (pages[j] == lnk) {
				pn = ls[0].parentNode;
				if (pn.className == 'item') pn.className = 'hoveritem';
				return j;
			}
		}
	}
	return 0;
}
/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
function showitem(t) { t.className = 'hoveritem'; }

function hideitem(t) { t.className = 'item'; }

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
function more(t) {
	t.parentNode.lastChild.className='inline'; t.className='hidden'; return false;
}

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
var indexloading = false; var root;

function initsearch(docroot) {
	root = docroot;
	document.getElementById('index').value = '';
	if ((typeof NAMES == 'undefined') && !indexloading) {
		indexloading = true;
		s = document.createElement('script');
		s.type = 'text/javascript'; s.src = root + '/resources/index.js';
		document.getElementsByTagName('head')[0].appendChild(s);
	}
}

function search() {
	field = document.getElementById('index');
	hidepopup();
	if ((typeof NAMES == 'undefined') || (field.value == '')) return;

	t = field.value; e = null;
	for (i = 0, n = 0; i < NAMES.length; i++) {
		if (NAMES[i].indexOf(t) != 0) continue;
		if (n == 0) {
			e = document.createElement('p'); e.id = 'popup'; e.className = 'result';
		}
		d = document.createElement('div');
		
		s = getlink(i, root) + ' &ndash; ' + TYPENAMES[TYPES[i]];
		if (PARENTS[i] != -1) s += ' ' + getlink(PARENTS[i], root);
		
		d.innerHTML = s;
		e.appendChild(d);
		n++; if (n > 8) break;
	}
	if (e != null) show(field, e);
}

function show(field, e) {
	x  = 0; y = field.offsetHeight + 1;
	for (c = field; c != null; c = c.offsetParent) {
		x += c.offsetLeft; y += c.offsetTop;
	}
	sh = document.body.scrollTop + document.body.clientHeight;
	e.style.position = 'absolute'; e.style.left = x; e.style.top = y;
	document.body.appendChild(e);
	if (y + e.offsetHeight > sh) {
		e.style.top = Math.max(0, y - field.offsetHeight - 2 - e.offsetHeight);
	}
}

function indexloaded() {
	addlistener(document.body, "click", hidepopup);
	addlistener(window, "blur", hidepopup);
	search();
}

function getlink(i, root) {
	pi = PARAMETERS[i];
	s = '<a href="' + root + '/';
	
	if (TYPES[i] == 0) s += pkglink(i) + 'package-summary.html';
	else if (TYPES[i] < 7) s += pkglink(PARENTS[i]) + NAMES[i] + '.html';
	else {
		s += pkglink(PARENTS[PARENTS[i]]) + NAMES[PARENTS[i]] + '.html#' + NAMES[i];
		if (pi != -1) s += '(' + SIGNATURES[pi * 2] + ')';
	}
	
	s += '">' + NAMES[i];
	if (pi != -1) s += '(' + SIGNATURES[pi * 2 + 1] + ')';
	
	return s + '</a>';
}

function pkglink(i) {
	s = NAMES[i];
	while ((j = s.indexOf('.')) != -1) s = s.substr(0, j) + '/' + s.substr(j + 1);
	return s + '/';
}

function hidepopup() {
	p = document.getElementById('popup');
	if (p != null) document.body.removeChild(p);
}
