import{S as e,i as t,s as a,l,f as o,u as s,w as r,x as n,d as i,H as c,A as f,I as d,r as h,P as u,K as x,e as m,t as p,k as y,c as g,a as v,g as b,n as $,b as w,Q as T,E,N as I,G as N,O as k,j as A,m as P,o as S,v as X}from"../chunks/vendor-cd5668a0.js";import{g as M,m as Y,L as D,C as L,b as V,s as F,e as R,f as W,p as j,d as O}from"../chunks/Login-93b755f0.js";function q(e){let t,a,l,s,r,n,c,f,d,h,u,x,A,P,S;return{c(){t=m("div"),a=m("div"),l=m("div"),s=m("div"),r=p("Overworld"),n=y(),c=m("div"),f=p("Nether"),d=y(),h=m("div"),u=p("End"),x=y(),A=m("canvas"),this.h()},l(e){t=g(e,"DIV",{class:!0});var o=v(t);a=g(o,"DIV",{class:!0});var m=v(a);l=g(m,"DIV",{class:!0});var p=v(l);s=g(p,"DIV",{class:!0});var y=v(s);r=b(y,"Overworld"),y.forEach(i),n=$(p),c=g(p,"DIV",{class:!0});var w=v(c);f=b(w,"Nether"),w.forEach(i),d=$(p),h=g(p,"DIV",{class:!0});var T=v(h);u=b(T,"End"),T.forEach(i),p.forEach(i),m.forEach(i),o.forEach(i),x=$(e),A=g(e,"CANVAS",{class:!0}),v(A).forEach(i),this.h()},h(){w(s,"class","text-gray-400 border-gray-400"),T(s,"border-b",0===e[2]),w(c,"class","text-gray-400 border-gray-400"),T(c,"border-b",1===e[2]),w(h,"class","text-gray-400 border-gray-400"),T(h,"border-b",2===e[2]),w(l,"class","w-full flex justify-around"),w(a,"class","md:w-2/3 w-full flex-col flex flex-grow"),w(t,"class","text-white p-5 w-full flex items-center flex-grow flex-col"),w(A,"class","w-screen h-screen absolute z-[-1] top-0 left-0")},m(i,m){o(i,t,m),E(t,a),E(a,l),E(l,s),E(s,r),E(l,n),E(l,c),E(c,f),E(l,d),E(l,h),E(h,u),o(i,x,m),o(i,A,m),e[9](A),P||(S=[I(s,"click",e[6]),I(c,"click",e[7]),I(h,"click",e[8])],P=!0)},p(e,t){4&t&&T(s,"border-b",0===e[2]),4&t&&T(c,"border-b",1===e[2]),4&t&&T(h,"border-b",2===e[2])},i:N,o:N,d(a){a&&i(t),a&&i(x),a&&i(A),e[9](null),P=!1,k(S)}}}function C(e){let t,a;return t=new D({}),{c(){A(t.$$.fragment)},l(e){P(t.$$.fragment,e)},m(e,l){S(t,e,l),a=!0},p:N,i(e){a||(n(t.$$.fragment,e),a=!0)},o(e){s(t.$$.fragment,e),a=!1},d(e){X(t,e)}}}function z(e){let t,a;return t=new L({}),{c(){A(t.$$.fragment)},l(e){P(t.$$.fragment,e)},m(e,l){S(t,e,l),a=!0},p:N,i(e){a||(n(t.$$.fragment,e),a=!0)},o(e){s(t.$$.fragment,e),a=!1},d(e){X(t,e)}}}function H(e){let t,a,c,f;const d=[z,C,q],u=[];function x(e,t){return e[3]?e[1]?2:1:0}return t=x(e),a=u[t]=d[t](e),{c(){a.c(),c=l()},l(e){a.l(e),c=l()},m(e,a){u[t].m(e,a),o(e,c,a),f=!0},p(e,[l]){let o=t;t=x(e),t===o?u[t].p(e,l):(h(),s(u[o],1,1,(()=>{u[o]=null})),r(),a=u[t],a?a.p(e,l):(a=u[t]=d[t](e),a.c()),n(a,1),a.m(c.parentNode,c))},i(e){f||(n(a),f=!0)},o(e){s(a),f=!1},d(e){u[t].d(e),e&&i(c)}}}function Z(e,t,a){let l,o,s,r,n,i,h;c(e,V,(e=>a(1,l=e))),c(e,F,(e=>a(4,o=e))),c(e,Y,(e=>a(2,s=e))),c(e,R,(e=>a(5,r=e))),c(e,W,(e=>a(20,n=e))),c(e,j,(e=>a(21,i=e))),c(e,O,(e=>a(3,h=e)));let m={},p=!1;f((()=>{p=!0,M()})),d((()=>{p&&(["pointerdown","pointerup","pointermove"].forEach((e=>document.removeEventListener(e,k))),document.removeEventListener("touchmove",E,{passive:!1}))}));let y,g,v={};const b={x:0,y:0,button:!1,wheel:0,lastX:0,lastY:0,drag:!1,mobile:!1};let $=0,w=0;const T={x:0,y:0,scale:1,apply(){g.setTransform(this.scale,0,0,this.scale,this.x,this.y)},scaleAt(e,t,a){this.scale*=a,this.x=e-(e-this.x)*a,this.y=t-(t-this.y)*a},toWorld(e,t,a={x:NaN,y:NaN}){const l=1/this.scale;return a.x=(e-this.x)*l,a.y=(t-this.y)*l,a},toReal(e,t,a={x:NaN,y:NaN}){const l=1/this.scale;return a.x=e/l+this.x,a.y=t/l+this.y,a}},E=e=>e.preventDefault();let I=[],N=-1;function k(e){if(y){const o=y.getBoundingClientRect();b.x=e.pageX-o.left-scrollX,b.y=e.pageY-o.top-scrollY,b.button="pointerdown"===e.type||"pointerup"!==e.type&&b.button,"pointerdown"===e.type&&I.push(e),"pointerup"===e.type&&(I=I.filter((t=>t.pointerId!==e.pointerId)));for(var t=0;t<I.length;t++)if(e.pointerId==I[t].pointerId){I[t]=e;break}if(2==I.length){b.button=!0,b.mobile=!0,b.x=I[0].pageX-o.left-scrollX,b.y=I[0].pageY-o.top-scrollY;var a=Math.abs(I[0].clientX-I[1].clientX),l=a-N;N>0&&(a>N&&(console.log("Pinch moving OUT -> Zoom in",e),b.wheel+=Math.abs(l)),a<N&&(console.log("Pinch moving IN -> Zoom out",e),b.wheel-=Math.abs(l))),N=a}"wheel"===e.type&&(b.wheel+=-e.deltaY,e.preventDefault())}}function A(){if(y){if(g.setTransform(1,0,0,1,0,0),g.globalAlpha=1,y&&($===y.clientWidth&&w===y.clientHeight||($=a(0,y.width=y.clientWidth,y),w=a(0,y.height=y.clientHeight,y))),g.clearRect(0,0,$,w),0!==b.wheel){let e=1;e=b.wheel<0?1/1.02:1.02,b.wheel*=.8,Math.abs(b.wheel)<1&&(b.wheel=0),T.scaleAt(b.x,b.y,e)}b.button?b.drag?(T.x+=b.x-b.lastX,T.y+=b.y-b.lastY,b.lastX=b.x,b.lastY=b.y):(b.lastX=b.x,b.lastY=b.y,b.drag=!0):b.drag&&(b.drag=!1),function(e=128){let t=1/T.scale,a=Math.pow(2,0|Math.log2(e*t)),l=Math.max($,w)*t+2*a,o=((-T.x*t-a)/a|0)*a,s=((-T.y*t-a)/a|0)*a;T.apply(),g.lineWidth=1,g.strokeStyle="#aaa",g.beginPath();for(let r=0;r<l;r+=a)g.moveTo(o+r,s),g.lineTo(o+r,s+l),g.moveTo(o,s+r),g.lineTo(o+l,s+r);g.setTransform(1,0,0,1,0,0),g.stroke()}(128),function(){g.strokeStyle="blue";let{x:e,y:t}=T.toReal(0,0);g.beginPath(),g.moveTo(e-10,t),g.lineTo(e+10,t),g.moveTo(e,t-10),g.lineTo(e,t+10),g.stroke()}(),function(e,t){g.lineWidth=1,g.strokeStyle="red",g.beginPath(),g.fillStyle="red",g.setTransform(1,0,0,1,0,0),g.moveTo(e-15,t),g.lineTo(e+15,t),g.moveTo(e,t-15),g.lineTo(e,t+15),g.stroke(),g.font="14px Arial";const a=T.toWorld(e,t);g.fillText(`${a.x.toFixed(0)} ${a.y.toFixed(0)}`,e+5,t-5)}(b.x,b.y),n[s].forEach((({x:e,z:t,type:a,name:l,id:o})=>{let s=T.toReal(e,t);g.fillStyle="blue",g.beginPath(),g.arc(s.x,s.y,5,0,2*Math.PI),g.fill(),g.fillStyle="#fff",g.font="14px Arial";let r=!1;if(m[a])try{g.drawImage(m[a],s.x-25,s.y-25,50,50),r=!0}catch(n){console.log("Error drawing image",n)}(T.scale>.5||!r)&&(v[a]?l?g.fillText(`${l} - ${v[a].name} (${o})`,s.x-25,s.y+35):g.fillText(`${v[a].name} (${o})`,s.x-25,s.y+40):l?g.fillText(`${l} (${o})`,s.x-25,s.y+35):g.fillText(`(${o})`,s.x-25,s.y+40),g.font="12px Arial",g.fillText(`${e} ${t}`,s.x-15,s.y+52))})),Object.entries(i).forEach((([e,{x:t,z:a,dimension:l}])=>{if(l!==s)return;let o=T.toReal(t,a);g.fillStyle="#fff",g.font="14px Arial",g.fillText(`${e} (${t.toFixed(0)}, ${a.toFixed(0)})`,o.x+10,o.y),g.fillStyle="yellow",g.beginPath(),g.arc(o.x,o.y,5,0,2*Math.PI),g.fill()}))}requestAnimationFrame(A)}return e.$$.update=()=>{22&e.$$.dirty&&l&&(o.requestPoints(s),o.requestTypes()),32&e.$$.dirty&&r.forEach((e=>{v[e.id]=e,e.image&&(m[e.id]=new Image,m[e.id].src=e.image)})),1&e.$$.dirty&&y&&null!==y&&(requestAnimationFrame(A),console.log(y),g=y.getContext("2d"),["pointerdown","pointerup","pointermove"].forEach((e=>document.addEventListener(e,k))),document.addEventListener("wheel",k,{passive:!1}),document.addEventListener("touchmove",E,{passive:!1}))},[y,l,s,h,o,r,()=>u(Y,s=0,s),()=>u(Y,s=1,s),()=>u(Y,s=2,s),function(e){x[e?"unshift":"push"]((()=>{y=e,a(0,y)}))}]}export default class extends e{constructor(e){super(),t(this,e,Z,H,a,{})}}
