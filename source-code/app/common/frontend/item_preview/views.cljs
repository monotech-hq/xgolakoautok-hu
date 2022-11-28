
(ns app.common.frontend.item-preview.views
    (:require [app.common.frontend.item-preview.prototypes :as item-preview.prototypes]
              [app.components.frontend.api                 :as components]
              [elements.api                                :as elements]
              [engines.item-preview.api                    :as item-preview]
              [plugins.reagent.api                         :refer [component?]]
              [random.api                                  :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- downloading-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [_ _]
  [elements/label {:color       :muted
                   :content     :downloading...
                   :font-size   :xs
                   :line-height :block}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-preview-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:item-link (namespaced map)(opt)
  ;   :item-path (vector)
  ;   :preview-element (component or symbol)
  ;   :transfer-id (keyword)}
  [preview-id {:keys [import-id-f item-link item-path preview-element transfer-id] :as preview-props}]
  ; BUG#9980
  ; Az item-preview/body komponenseket különböző azonosítókkal szükséges meghívni,
  ; hogy külön tudják kezelni az egyes elemekhez letöltött adatokat!
  ;
  ; Generált azonosítót nem lehetséges használni, mert a komponens újrarenderelődésekor
  ; (pl. a preview-props térkép megváltozásakor), az item-preview engine példánya
  ; elveszítené a kapcsolatot a letöltött adatokkal!
  (let [id (import-id-f item-link)]
       [item-preview/body (keyword id)
                          {:error-element   [components/error-label {:error :the-content-has-been-broken}]
                           :ghost-element   [downloading-label preview-id preview-props]
                           :preview-element [preview-element   preview-id preview-props]
                           :item-id         id
                           :item-path       item-path
                           :transfer-id     transfer-id}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-preview-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? info-text label]}]
  (if label [elements/label {:content     label
                             :disabled?   disabled?
                             :info-text   info-text
                             :line-height :block}]))

(defn- item-preview-placeholder
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [preview-id {:keys [disabled? placeholder] :as preview-props}]
  ; XXX#9010
  ; Az item-preview komponensnek komponensként vagy szimbólumnként is át lehet
  ; adni a placeholder tulajdonságot.
  (cond (component? placeholder) (conj placeholder preview-id preview-props)
        (fn?        placeholder) [placeholder      preview-id preview-props]
        (some?      placeholder) [elements/label {:color       :muted
                                                  :content     placeholder
                                                  :disabled?   disabled?
                                                  :font-size   :xs
                                                  :line-height :block
                                                  :selectable? true}]))

(defn- item-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:indent (map)(opt)
  ;   :import-id-f (function)
  ;   :item-link (namespaced map)(opt)}
  [preview-id {:keys [import-id-f indent item-link] :as preview-props}]
  ; BUG#8860
  ; Akkor jeleníti meg az item-preview-body komponenst ha az item-link értéke tartalmazza
  ; az id kulcsot, így ha az item-link értéke nil, vagy hibás/hiányos, akkor a placeholder
  ; felirat jelenik meg.
  [elements/blank preview-id
                  {:content [:<> [item-preview-label preview-id preview-props]
                                 (if-let [id (import-id-f item-link)]
                                         [item-preview-body        preview-id preview-props]
                                         [item-preview-placeholder preview-id preview-props])]
                   :indent  indent}])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :import-id-f (function)(opt)
  ;    Default: (fn [{:keys [id] :as item-link}] id)
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :item-link (namespaced map)(opt)
  ;   :item-path (vector)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :preview-element (component or symbol)
  ;   :transfer-id (keyword)(opt)
  ;    Az elemet megjelenítő engines.item-preview.api/body komponens paramétere.
  ;
  ; @usage
  ;  [item-preview {...}]
  ;
  ; @usage
  ;  [item-preview :my-item-preview {...}]
  ;
  ; @usage
  ;  (defn my-preview-element [preview-id preview-props] ...)
  ;  [item-preview :my-item-preview {:preview-element #'my-preview-element}]
  ;
  ; @usage
  ;  [item-preview :my-item-preview {:placeholder "My placeholder"}]
  ;
  ; @usage
  ;  (defn my-placeholder [preview-id preview-props] ...)
  ;  [item-preview :my-item-preview {:placeholder #'my-placeholder}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (item-preview.prototypes/preview-props-prototype preview-id preview-props)]
        [item-preview preview-id preview-props])))
