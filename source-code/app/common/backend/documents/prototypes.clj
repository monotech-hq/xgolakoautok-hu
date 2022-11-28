
(ns app.common.backend.documents.prototypes
    (:require [json.api   :as json]
              [x.user.api :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-document-prototype
  ; @param (map) request
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  [request document]
  ; - A dokumentum added-by és modified-by értékeit feltölti a hivatkozott
  ;   felhasználó publikus adataival.
  (x.user/fill-document request document))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  ; - A get-document-prototype függvény által alkalmazott x.user/fill-document
  ;   függvény által a dokumentumba írt felhasználói adatok eltávolítása
  ; - A string típusú értékek vágása
  ; - A kliens-oldali mezők által string típusként tárolt egész számok
  ;   integer típusra alakítása
  ; - Az üres értékek eltávolítása (pl. "", nil, [], {}, ())
  (->> document (x.user/updated-document-prototype request)
                (x.user/clean-document             request)
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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn updated-edn-prototype
  ; @param (map) request
  ; @param (map) content
  [request content]
  ; - A string típusú értékek vágása
  ; - A kliens-oldali mezők által string típusként tárolt egész számok
  ;   integer típusra alakítása
  ; - Az üres értékek eltávolítása (pl. "", nil, [], {}, ())
  (->> content (json/trim-values)
               (json/parse-number-values)
               (json/remove-blank-values)))
