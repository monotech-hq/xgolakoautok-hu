
(ns app.schemes.frontend.scheme-form.views
    (:require [elements.api      :as elements]
              [vector.api :as vector]
              [re-frame.api      :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- scheme-field-body-text-field
  ; @param (keyword) scheme-id
  ; @param (map) form-props
  ; @param (integer) field-dex
  ; @param (namespaced map) field-item
  [scheme-id {:keys [suggestions-path value-path]} field-dex {:field/keys [field-id protected? unit]}]
  ; BUG#3007
  ; Az egyszerű szövegmező kimete is string típus vektorban, így a mező típusa szabadon állítható
  ; már kitöltött értékek esetén is
  ; Pl.: Ha az egyszerű szövegmezők értéke nem string típus vektorban lenne, hanem egyszerűen
  ;      csak string típus lenne, akkor kitöltött mező esetén, ha a felhasználó többértékű szövegmezőre
  ;      állítaná a mező típusát, akkor a multi-field az egyszerű string típust karakterenként külön-külön
  ;      mezőben jelenítené meg!
  [elements/text-field {:autofocus?     (= field-dex 0)
                        :disabled?      false
                        :emptiable?     false
                        :end-adornments [{:label unit :color :muted}]
                        :indent         {:vertical :s :top :xxs}
                        :options-path   (if suggestions-path (vector/conj-item suggestions-path field-id))
                        :value-path     (if value-path       (vector/conj-item value-path       field-id 0))}])

(defn- scheme-field-body-multi-field
  ; @param (keyword) scheme-id
  ; @param (map) form-props
  ; @param (integer) field-dex
  ; @param (namespaced map) field-item
  [scheme-id {:keys [suggestions-path value-path]} field-dex {:field/keys [field-id protected? unit]}]
  [elements/multi-field {:autofocus?     (= field-dex 0)
                         :disabled?      false
                         :emptiable?     false
                         :end-adornments [{:label unit :color :muted}]
                         :indent         {:vertical :s :top :xxs}
                         :options-path   (if suggestions-path (vector/conj-item suggestions-path field-id))
                         :value-path     (if value-path       (vector/conj-item value-path       field-id))}])

(defn- scheme-field-body
  ; @param (keyword) scheme-id
  ; @param (map) form-props
  ; @param (integer) field-dex
  ; @param (namespaced map) field-item
  ;  {:field/type (keyword)
  ;    :single-field, :multi-field}
  [scheme-id form-props field-dex {:field/keys [type] :as field-item}]
  (case type :multi-field [scheme-field-body-multi-field scheme-id form-props field-dex field-item]
                          [scheme-field-body-text-field  scheme-id form-props field-dex field-item]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- scheme-field-menu-icon-button
  ; @param (keyword) scheme-id
  ; @param (map) form-props
  ; @param (integer) field-dex
  ; @param (namespaced map) field-item
  ;  {:field-id (keyword)
  ;   :protected? (boolean)(opt)}
  [scheme-id _ _ {:field/keys [field-id protected?]}]
  [elements/icon-button {:disabled? protected?
                         :icon      :more_horiz
                         :height    :s
                         :indent    {:right :xs}
                         :on-click  [:schemes.field-menu/render-menu! scheme-id field-id]}])

(defn- scheme-field-label
  ; @param (keyword) scheme-id
  ; @param (map) form-props
  ; @param (integer) field-dex
  ; @param (namespaced map) field-item
  [_ _ _ {:field/keys [name]}]
  [elements/label {:color            :default
                   :content          name
                   :horizontal-align :right
                   :indent           {:left :s}
                   :line-height      :block}])

(defn- scheme-field-header
  ; @param (keyword) scheme-id
  ; @param (map) form-props
  ; @param (integer) field-dex
  ; @param (namespaced map) field-item
  [scheme-id form-props field-dex field-item]
  [:div {:style {:display :flex :align-items :center}}
        [scheme-field-label scheme-id form-props field-dex field-item]
        [:div {:style {:display :flex :flex-grow 1 :justify-content :flex-end}}
              [scheme-field-menu-icon-button scheme-id form-props field-dex field-item]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- scheme-form-item
  ; @param (keyword) scheme-id
  ; @param (map) form-props
  ; @param (integer) field-dex
  ; @param (namespaced map) field-item
  [scheme-id form-props field-dex field-item]
  [:div {:style {:border-top (if-not (= 0 field-dex) "1px solid var( --border-color-highlight )")
                 :padding "24px 0"}}
        [scheme-field-header scheme-id form-props field-dex field-item]
        [scheme-field-body   scheme-id form-props field-dex field-item]])

(defn- scheme-form
  ; @param (keyword) scheme-id
  ; @param (map) form-props
  [scheme-id form-props]
  (let [scheme-fields @(r/subscribe [:schemes.form-handler/get-scheme-fields scheme-id])]
       [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "0"}}
             (letfn [(f [items field-dex field-item]
                        (conj items [scheme-form-item scheme-id form-props field-dex field-item]))]
                    (reduce-kv f [:<>] scheme-fields))]))

(defn view
  ; @param (keyword) scheme-id
  ; @param (map) form-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :suggestions-path (vector)(opt)
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [scheme-form :my-scheme {:value-path [:my-values]}]
  [scheme-id form-props]
  [scheme-form scheme-id form-props])
