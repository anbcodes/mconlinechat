function t(){}function n(t,n){for(const e in n)t[e]=n[e];return t}function e(t){return t()}function o(){return Object.create(null)}function r(t){t.forEach(e)}function i(t){return"function"==typeof t}function c(t,n){return t!=t?n==n:t!==n||t&&"object"==typeof t||"function"==typeof t}function s(n,e,o){n.$$.on_destroy.push(function(n,...e){if(null==n)return t;const o=n.subscribe(...e);return o.unsubscribe?()=>o.unsubscribe():o}(e,o))}function u(t,n,e,o){if(t){const r=a(t,n,e,o);return t[0](r)}}function a(t,e,o,r){return t[1]&&r?n(o.ctx.slice(),t[1](r(e))):o.ctx}function l(t,n,e,o,r,i,c){const s=function(t,n,e,o){if(t[2]&&o){const r=t[2](o(e));if(void 0===n.dirty)return r;if("object"==typeof r){const t=[],e=Math.max(n.dirty.length,r.length);for(let o=0;o<e;o+=1)t[o]=n.dirty[o]|r[o];return t}return n.dirty|r}return n.dirty}(n,o,r,i);if(s){const r=a(n,e,o,c);t.p(r,s)}}function f(t,n,e=n){return t.set(e),n}let d,h=!1;function p(t,n,e,o){for(;t<n;){const r=t+(n-t>>1);e(r)<=o?t=r+1:n=r}return t}function _(t,n){h?(!function(t){if(t.hydrate_init)return;t.hydrate_init=!0;const n=t.childNodes,e=new Int32Array(n.length+1),o=new Int32Array(n.length);e[0]=-1;let r=0;for(let u=0;u<n.length;u++){const t=p(1,r+1,(t=>n[e[t]].claim_order),n[u].claim_order)-1;o[u]=e[t]+1;const i=t+1;e[i]=u,r=Math.max(i,r)}const i=[],c=[];let s=n.length-1;for(let u=e[r]+1;0!=u;u=o[u-1]){for(i.push(n[u-1]);s>=u;s--)c.push(n[s]);s--}for(;s>=0;s--)c.push(n[s]);i.reverse(),c.sort(((t,n)=>t.claim_order-n.claim_order));for(let u=0,a=0;u<c.length;u++){for(;a<i.length&&c[u].claim_order>=i[a].claim_order;)a++;const n=a<i.length?i[a]:null;t.insertBefore(c[u],n)}}(t),(void 0===t.actual_end_child||null!==t.actual_end_child&&t.actual_end_child.parentElement!==t)&&(t.actual_end_child=t.firstChild),n!==t.actual_end_child?t.insertBefore(n,t.actual_end_child):t.actual_end_child=n.nextSibling):n.parentNode!==t&&t.appendChild(n)}function m(t,n,e){h&&!e?_(t,n):(n.parentNode!==t||e&&n.nextSibling!==e)&&t.insertBefore(n,e||null)}function g(t){t.parentNode.removeChild(t)}function $(t,n){for(let e=0;e<t.length;e+=1)t[e]&&t[e].d(n)}function b(t){return document.createElement(t)}function y(t){return document.createTextNode(t)}function x(){return y(" ")}function v(){return y("")}function w(t,n,e,o){return t.addEventListener(n,e,o),()=>t.removeEventListener(n,e,o)}function E(t,n,e){null==e?t.removeAttribute(n):t.getAttribute(n)!==e&&t.setAttribute(n,e)}function k(t){return Array.from(t.childNodes)}function N(t,n,e,o,r=!1){void 0===t.claim_info&&(t.claim_info={last_index:0,total_claimed:0});const i=(()=>{for(let o=t.claim_info.last_index;o<t.length;o++){const i=t[o];if(n(i))return e(i),t.splice(o,1),r||(t.claim_info.last_index=o),i}for(let o=t.claim_info.last_index-1;o>=0;o--){const i=t[o];if(n(i))return e(i),t.splice(o,1),r?t.claim_info.last_index--:t.claim_info.last_index=o,i}return o()})();return i.claim_order=t.claim_info.total_claimed,t.claim_info.total_claimed+=1,i}function A(t,n,e,o){return N(t,(t=>t.nodeName===n),(t=>{const n=[];for(let o=0;o<t.attributes.length;o++){const r=t.attributes[o];e[r.name]||n.push(r.name)}n.forEach((n=>t.removeAttribute(n)))}),(()=>o?function(t){return document.createElementNS("http://www.w3.org/2000/svg",t)}(n):b(n)))}function j(t,n){return N(t,(t=>3===t.nodeType),(t=>{t.data=""+n}),(()=>y(n)),!0)}function S(t){return j(t," ")}function O(t,n){n=""+n,t.wholeText!==n&&(t.data=n)}function B(t,n){t.value=null==n?"":n}function C(t,n,e){t.classList[e?"add":"remove"](n)}function L(t){d=t}function M(){if(!d)throw new Error("Function called outside component initialization");return d}function I(t){M().$$.on_mount.push(t)}function T(t){M().$$.after_update.push(t)}function q(t){M().$$.on_destroy.push(t)}function z(t,n){M().$$.context.set(t,n)}const F=[],P=[],D=[],G=[],H=Promise.resolve();let J=!1;function K(){J||(J=!0,H.then(W))}function Q(){return K(),H}function R(t){D.push(t)}let U=!1;const V=new Set;function W(){if(!U){U=!0;do{for(let t=0;t<F.length;t+=1){const n=F[t];L(n),X(n.$$)}for(L(null),F.length=0;P.length;)P.pop()();for(let t=0;t<D.length;t+=1){const n=D[t];V.has(n)||(V.add(n),n())}D.length=0}while(F.length);for(;G.length;)G.pop()();J=!1,U=!1,V.clear()}}function X(t){if(null!==t.fragment){t.update(),r(t.before_update);const n=t.dirty;t.dirty=[-1],t.fragment&&t.fragment.p(t.ctx,n),t.after_update.forEach(R)}}const Y=new Set;let Z;function tt(){Z={r:0,c:[],p:Z}}function nt(){Z.r||r(Z.c),Z=Z.p}function et(t,n){t&&t.i&&(Y.delete(t),t.i(n))}function ot(t,n,e,o){if(t&&t.o){if(Y.has(t))return;Y.add(t),Z.c.push((()=>{Y.delete(t),o&&(e&&t.d(1),o())})),t.o(n)}}function rt(t,n){const e={},o={},r={$$scope:1};let i=t.length;for(;i--;){const c=t[i],s=n[i];if(s){for(const t in c)t in s||(o[t]=1);for(const t in s)r[t]||(e[t]=s[t],r[t]=1);t[i]=s}else for(const t in c)r[t]=1}for(const c in o)c in e||(e[c]=void 0);return e}function it(t){return"object"==typeof t&&null!==t?t:{}}function ct(t){t&&t.c()}function st(t,n){t&&t.l(n)}function ut(t,n,o,c){const{fragment:s,on_mount:u,on_destroy:a,after_update:l}=t.$$;s&&s.m(n,o),c||R((()=>{const n=u.map(e).filter(i);a?a.push(...n):r(n),t.$$.on_mount=[]})),l.forEach(R)}function at(t,n){const e=t.$$;null!==e.fragment&&(r(e.on_destroy),e.fragment&&e.fragment.d(n),e.on_destroy=e.fragment=null,e.ctx=[])}function lt(n,e,i,c,s,u,a=[-1]){const l=d;L(n);const f=n.$$={fragment:null,ctx:null,props:u,update:t,not_equal:s,bound:o(),on_mount:[],on_destroy:[],on_disconnect:[],before_update:[],after_update:[],context:new Map(l?l.$$.context:e.context||[]),callbacks:o(),dirty:a,skip_bound:!1};let p=!1;if(f.ctx=i?i(n,e.props||{},((t,e,...o)=>{const r=o.length?o[0]:e;return f.ctx&&s(f.ctx[t],f.ctx[t]=r)&&(!f.skip_bound&&f.bound[t]&&f.bound[t](r),p&&function(t,n){-1===t.$$.dirty[0]&&(F.push(t),K(),t.$$.dirty.fill(0)),t.$$.dirty[n/31|0]|=1<<n%31}(n,t)),e})):[],f.update(),p=!0,r(f.before_update),f.fragment=!!c&&c(f.ctx),e.target){if(e.hydrate){h=!0;const t=k(e.target);f.fragment&&f.fragment.l(t),t.forEach(g)}else f.fragment&&f.fragment.c();e.intro&&et(n.$$.fragment),ut(n,e.target,e.anchor,e.customElement),h=!1,W()}L(l)}class ft{$destroy(){at(this,1),this.$destroy=t}$on(t,n){const e=this.$$.callbacks[t]||(this.$$.callbacks[t]=[]);return e.push(n),()=>{const t=e.indexOf(n);-1!==t&&e.splice(t,1)}}$set(t){var n;this.$$set&&(n=t,0!==Object.keys(n).length)&&(this.$$.skip_bound=!0,this.$$set(t),this.$$.skip_bound=!1)}}const dt=[];function ht(n,e=t){let o;const r=[];function i(t){if(c(n,t)&&(n=t,o)){const t=!dt.length;for(let e=0;e<r.length;e+=1){const t=r[e];t[1](),dt.push(t,n)}if(t){for(let t=0;t<dt.length;t+=2)dt[t][0](dt[t+1]);dt.length=0}}}return{set:i,update:function(t){i(t(n))},subscribe:function(c,s=t){const u=[c,s];return r.push(u),1===r.length&&(o=e(i)||t),c(n),()=>{const t=r.indexOf(u);-1!==t&&r.splice(t,1),0===r.length&&(o(),o=null)}}}}export{n as A,tt as B,ht as C,u as D,_ as E,l as F,t as G,s as H,q as I,Q as J,P as K,$ as L,B as M,w as N,r as O,f as P,C as Q,ft as S,k as a,E as b,A as c,g as d,b as e,m as f,j as g,O as h,lt as i,ct as j,x as k,v as l,st as m,S as n,ut as o,rt as p,it as q,ot as r,c as s,y as t,nt as u,et as v,at as w,z as x,T as y,I as z};