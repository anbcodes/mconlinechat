import{S as e,i as t,s as n,l as a,f as s,u as r,w as l,x as o,d as c,H as i,A as f,I as u,r as h,J as m,K as d,e as g,t as v,k as p,c as w,a as $,g as I,n as y,b as x,E,h as D,L as T,M as F,N as V,G as b,O as M,j as R,m as k,o as L,v as N,P as j}from"../chunks/vendor-a0e0cf93.js";import{g as P,L as S,C,r as H,a as O,s as U,c as Y,h as _,b as q,d as A}from"../chunks/Login-ca7bd3ad.js";function B(e,t,n){const a=e.slice();return a[22]=t[n],a[23]=t,a[24]=n,a}function G(e,t,n){const a=e.slice();return a[25]=t[n],a[26]=t,a[27]=n,a}function J(e){let t,n,a,r,l,o,i,f,u,h,m,d=[...e[5]].reverse(),D=[];for(let s=0;s<d.length;s+=1)D[s]=Q(B(e,d,s));return{c(){t=g("div"),n=g("div"),a=g("div");for(let e=0;e<D.length;e+=1)D[e].c();r=p(),l=g("div"),o=g("input"),i=p(),f=g("button"),u=v("Send"),this.h()},l(e){t=w(e,"DIV",{class:!0});var s=$(t);n=w(s,"DIV",{class:!0});var h=$(n);a=w(h,"DIV",{class:!0});var m=$(a);for(let t=0;t<D.length;t+=1)D[t].l(m);m.forEach(c),r=y(h),l=w(h,"DIV",{class:!0});var d=$(l);o=w(d,"INPUT",{class:!0}),i=y(d),f=w(d,"BUTTON",{class:!0});var g=$(f);u=I(g,"Send"),g.forEach(c),d.forEach(c),h.forEach(c),s.forEach(c),this.h()},h(){x(a,"class","flex-grow overflow-scroll p-1"),x(o,"class","bg-gray-700 rounded p-1 flex-grow"),x(f,"class","mx-4 p-1 px-2 rounded bg-gray-700 hover:bg-gray-800"),x(l,"class","flex mt-2"),x(n,"class","w-2/3 bg-gray-800 rounded m-5 p-5 h-[50rem] flex-col flex"),x(t,"class","w-full flex justify-center")},m(c,d){s(c,t,d),E(t,n),E(n,a);for(let e=0;e<D.length;e+=1)D[e].m(a,null);e[12](a),E(n,r),E(n,l),E(l,o),F(o,e[4]),E(l,i),E(l,f),E(f,u),h||(m=[V(o,"input",e[13]),V(o,"keydown",e[14]),V(f,"click",e[15])],h=!0)},p(e,t){if(418&t){let n;for(d=[...e[5]].reverse(),n=0;n<d.length;n+=1){const s=B(e,d,n);D[n]?D[n].p(s,t):(D[n]=Q(s),D[n].c(),D[n].m(a,null))}for(;n<D.length;n+=1)D[n].d(1);D.length=d.length}16&t&&o.value!==e[4]&&F(o,e[4])},i:b,o:b,d(n){n&&c(t),T(D,n),e[12](null),h=!1,M(m)}}}function K(e){let t,n;return t=new S({}),{c(){R(t.$$.fragment)},l(e){k(t.$$.fragment,e)},m(e,a){L(t,e,a),n=!0},p:b,i(e){n||(o(t.$$.fragment,e),n=!0)},o(e){r(t.$$.fragment,e),n=!1},d(e){N(t,e)}}}function W(e){let t,n;return t=new C({}),{c(){R(t.$$.fragment)},l(e){k(t.$$.fragment,e)},m(e,a){L(t,e,a),n=!0},p:b,i(e){n||(o(t.$$.fragment,e),n=!0)},o(e){r(t.$$.fragment,e),n=!1},d(e){N(t,e)}}}function z(e){let t,n,a,r,l,o,i,f,u=e[8](e[25])+"",h=e[24],m=e[27],d=e[7](e[25].sent)+"";const T=()=>e[11](n,h,m),F=()=>e[11](null,h,m);return{c(){t=g("div"),n=g("div"),a=v(u),r=p(),l=g("div"),o=v("- "),i=v(d),f=p(),this.h()},l(e){t=w(e,"DIV",{class:!0});var s=$(t);n=w(s,"DIV",{});var h=$(n);a=I(h,u),h.forEach(c),r=y(s),l=w(s,"DIV",{class:!0});var m=$(l);o=I(m,"- "),i=I(m,d),m.forEach(c),f=y(s),s.forEach(c),this.h()},h(){x(l,"class","text-gray-400 ml-5"),x(t,"class","p-1")},m(e,c){s(e,t,c),E(t,n),E(n,a),T(),E(t,r),E(t,l),E(l,o),E(l,i),E(t,f)},p(t,n){e=t,32&n&&u!==(u=e[8](e[25])+"")&&D(a,u),h===e[24]&&m===e[27]||(F(),h=e[24],m=e[27],T()),32&n&&d!==(d=e[7](e[25].sent)+"")&&D(i,d)},d(e){e&&c(t),F()}}}function Q(e){let t,n=[...e[22]].reverse(),r=[];for(let a=0;a<n.length;a+=1)r[a]=z(G(e,n,a));return{c(){for(let e=0;e<r.length;e+=1)r[e].c();t=a()},l(e){for(let t=0;t<r.length;t+=1)r[t].l(e);t=a()},m(e,n){for(let t=0;t<r.length;t+=1)r[t].m(e,n);s(e,t,n)},p(e,a){if(418&a){let s;for(n=[...e[22]].reverse(),s=0;s<n.length;s+=1){const l=G(e,n,s);r[s]?r[s].p(l,a):(r[s]=z(l),r[s].c(),r[s].m(t.parentNode,t))}for(;s<r.length;s+=1)r[s].d(1);r.length=n.length}},d(e){T(r,e),e&&c(t)}}}function X(e){let t,n,i,f;const u=[W,K,J],m=[];function d(e,t){return e[3]?e[2]?2:1:0}return t=d(e),n=m[t]=u[t](e),{c(){n.c(),i=a()},l(e){n.l(e),i=a()},m(e,n){m[t].m(e,n),s(e,i,n),f=!0},p(e,[a]){let s=t;t=d(e),t===s?m[t].p(e,a):(h(),r(m[s],1,1,(()=>{m[s]=null})),l(),n=m[t],n?n.p(e,a):(n=m[t]=u[t](e),n.c()),o(n,1),n.m(i.parentNode,i))},i(e){f||(o(n),f=!0)},o(e){r(n),f=!1},d(e){m[t].d(e),e&&c(i)}}}function Z(e,t,n){let a,s,r,l,o,c,h;i(e,H,(e=>n(16,a=e))),i(e,O,(e=>n(17,s=e))),i(e,U,(e=>n(10,r=e))),i(e,Y,(e=>n(5,l=e))),i(e,_,(e=>n(18,o=e))),i(e,q,(e=>n(2,c=e))),i(e,A,(e=>n(3,h=e)));var g=this&&this.__awaiter||function(e,t,n,a){return new(n||(n=Promise))((function(s,r){function l(e){try{c(a.next(e))}catch(t){r(t)}}function o(e){try{c(a.throw(e))}catch(t){r(t)}}function c(e){var t;e.done?s(e.value):(t=e.value,t instanceof n?t:new n((function(e){e(t)}))).then(l,o)}c((a=a.apply(e,t||[])).next())}))};let v,p=[],w="",$=[],I=!1;const y=()=>{v.scrollTop<200&&!a&&!s&&(j(H,a=!0,a),r.requestHistory(l.length,o))};f((()=>{P(),n(9,I=!0)})),u((()=>{$.forEach((e=>{r.remove(e)})),v&&v.removeEventListener("scroll",y)}));const x=e=>{c&&r.sendChatMessage(e)};u((()=>{}));return e.$$.update=()=>{1551&e.$$.dirty&&I&&c&&h&&d().then((()=>{console.log(c),$.push(r.on("chatMessage",(()=>g(void 0,void 0,void 0,(function*(){yield d(),p.slice(-1)[0].scrollIntoView()}))))),$.push(r.on("historyData",(({page:e,offset:t})=>g(void 0,void 0,void 0,(function*(){yield d(),0===e?p.slice(-1)[0].scrollIntoView():p.slice(100)[0]&&p.slice(100)[0].scrollIntoView()}))))),v.addEventListener("scroll",y)}))},[v,p,c,h,w,l,x,e=>{let t=+new Date-e,n=Math.floor(t/1e3),a=Math.floor(t/1e3/60),s=Math.floor(t/1e3/60/60),r=Math.floor(t/1e3/60/60/24),l=Math.floor(t/1e3/60/60/24/7),o=new Date(t).getMonth()+12*(new Date(t).getFullYear()-1970),c=new Date(t).getFullYear()-23640;return c>0?(new Intl.RelativeTimeFormat).format(-c,"year"):o>0?(new Intl.RelativeTimeFormat).format(-o,"month"):l>0?(new Intl.RelativeTimeFormat).format(-l,"week"):r>0?(new Intl.RelativeTimeFormat).format(-r,"days"):s>0?(new Intl.RelativeTimeFormat).format(-s,"hours"):a>0?(new Intl.RelativeTimeFormat).format(-a,"minutes"):n>0?(new Intl.RelativeTimeFormat).format(-n,"seconds"):(new Intl.RelativeTimeFormat).format(-0,"seconds")},e=>{let t=e.message.replace(/^Â/g,"").replace(/§./g,"");return""!==e.sender?e.fromWebsite?`[${e.sender}] ${t}`:`<${e.sender}> ${t}`:`${t}`},I,r,function(e,t,a){m[e?"unshift":"push"]((()=>{p[100*t+a]=e,n(1,p)}))},function(e){m[e?"unshift":"push"]((()=>{v=e,n(0,v)}))},function(){w=this.value,n(4,w)},e=>{"Enter"===e.key&&(x(w),n(4,w=""))},()=>{x(w),n(4,w="")}]}export default class extends e{constructor(e){super(),t(this,e,Z,X,n,{})}}
