# -*- coding: utf-8 -*-
"""Construye la matriz ejercicio <-> criterio de evaluacion leyendo el proyecto."""
import io, os, re
from ce_catalogo import CE

_AQUI = os.path.dirname(os.path.abspath(__file__))
_CAND = [os.path.join(_AQUI, "..", "src", "com", "ifcd0112", "ejercicios"),
         os.path.join(_AQUI, "proy", "src", "com", "ifcd0112", "ejercicios")]
BASE = next(os.path.normpath(c) for c in _CAND if os.path.isdir(c))

def leer():
    ejs = []
    for pkg in ("uf2404", "uf2405", "uf2406", "ampliacion"):
        for raiz, _, fich in os.walk(os.path.join(BASE, pkg)):
            for f in sorted(fich):
                if not re.match(r"^(Ej\d+|A\d+)\w*\.java$", f):
                    continue
                txt = io.open(os.path.join(raiz, f), encoding="utf-8").read()
                cab = re.search(r"^ \* ((?:UF|AMPLIACION).*?): (.*?)\.?$", txt, re.M)
                ces = re.search(r"Criterios de evaluacion: (.*?)</p>", txt)
                extra = re.findall(r"tambien.*?(CE\d+\.\d+)", txt, re.S)
                cab_t = cab.group(1)
                uf = re.search(r"UF24\d\d", cab_t).group(0)
                num = int(re.search(r"EJERCICIO A?(\d+)", cab_t).group(1))
                amp = cab_t.startswith("AMPLIACION")
                ejs.append({
                    "uf": uf, "amp": amp, "num": num,
                    "codigo": ("A%d" % num) if amp else str(num),
                    "titulo": cab.group(2).strip(),
                    "ces": [c.strip() for c in ces.group(1).split(",")],
                    "extra": sorted(set(extra)),
                    "fichero": os.path.relpath(os.path.join(raiz, f), os.path.join(BASE, "..", "..", "..", "..")),
                    "bloque": os.path.basename(raiz),
                })
    ejs.sort(key=lambda e: (e["uf"], e["amp"], e["num"]))
    return ejs

def cobertura(ejs):
    """Devuelve {uf: {ce: [codigos]}} contando solo etiquetas oficiales."""
    m = {uf: {ce: [] for ce in CE[uf] if ce.startswith("CE")} for uf in CE}
    for e in ejs:
        for ce in e["ces"]:
            if ce in m[e["uf"]]:
                m[e["uf"]][ce].append(e)
            else:
                print("AVISO: %s %s declara %s, que no existe en el RD" % (e["uf"], e["codigo"], ce))
    return m

if __name__ == "__main__":
    ejs = leer()
    print("Ejercicios leidos:", len(ejs))
    m = cobertura(ejs)
    tot_cub = tot = 0
    for uf in ("UF2404", "UF2405", "UF2406"):
        ces = [c for c in CE[uf] if c.startswith("CE")]
        cuad = [c for c in ces if any(not e["amp"] for e in m[uf][c])]
        todo = [c for c in ces if m[uf][c]]
        falta = [c for c in ces if not m[uf][c]]
        print("%s: %d CE | cuaderno %d | +ampliacion %d | sin cubrir %d %s"
              % (uf, len(ces), len(cuad), len(todo), len(falta), falta))
        tot_cub += len(todo); tot += len(ces)
    print("TOTAL cubiertos: %d / %d" % (tot_cub, tot))
    for e in ejs:
        if e["extra"]:
            print("  nota de cobertura extra:", e["uf"], e["codigo"], e["extra"])
