
(ns app.common.backend.prototypes
    (:require [mid-fruits.json   :as json]
              [x.server-user.api :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; XXX#0780 (app.common.backend.prototypes)
; Az added-document-prototype, updated-document-prototype és duplicated-document-prototype
; függvények a modulok (adatbázisba dokumentumot író) mutation függvényeinek prototípusai.
;
; Amíg funkcionalitásukban ennyire hasonlítanak a moduluk és nem szükséges modulonként,
; vagy mutation függvényenként meghatározni, hogy milyen műveleteket végezzenek a prototípus
; függvények, addig ezek a közös függvények lesznek alkalmazva.

(defn added-document-prototype
  ; @param (map) request
  ; @param (namespaced map) document
  [request document]
  ; - Utolsó modosítás dátumának és a felhasználó azonosítójának aktualizálása
  ; - Hozzáadás dátumának és a felhasználó azonosítójának aktualizálása
  ; - A string típusú értékek vágása
  ; - A kliens-oldali mezők által string típusként tárolt egész számok
  ;   integer típusra alakítása
  ; - Az üres értékek eltávolítása (pl. "", nil, [], {}, ())
  (->> document (x.user/added-document-prototype request)
                (json/trim-values)
                (json/parse-number-values)
                (json/remove-blank-values)))

(defn updated-document-prototype
  ; @param (map) request
  ; @param (namespaced map) document
  [request document]
  ; - Utolsó modosítás dátumának és a felhasználó azonosítójának aktualizálása
  ; - A string típusú értékek vágása
  ; - A kliens-oldali mezők által string típusként tárolt egész számok
  ;   integer típusra alakítása
  ; - Az üres értékek eltávolítása (pl. "", nil, [], {}, ())
  (->> document (x.user/updated-document-prototype request)
                (json/trim-values)
                (json/parse-number-values)
                (json/remove-blank-values)))

(defn duplicated-document-prototype
  ; @param (map) request
  ; @param (namespaced map) document
  [request document]
  ; - Utolsó modosítás dátumának és a felhasználó azonosítójának aktualizálása
  ; - A string típusú értékek vágása
  ; - A kliens-oldali mezők által string típusként tárolt egész számok
  ;   integer típusra alakítása
  ; - Az üres értékek eltávolítása (pl. "", nil, [], {}, ())
  (->> document (x.user/duplicated-document-prototype request)
                (json/trim-values)
                (json/parse-number-values)
                (json/remove-blank-values)))
