
(ns app.components.frontend.item-info.views
    (:require [app.components.frontend.surface-description.views :as surface-description.views]
              [elements.api                                      :as elements]
              [random.api                                        :as random]
              [re-frame.api                                      :as r]
              [string.api                                        :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-info
  ; @param (keyword) info-id
  ; @param (map) info-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :item-path (vector)}
  [info-id {:keys [disabled? indent item-path] :as info-props}]
  (let [item   @(r/subscribe [:x.db/get-item item-path])
        modified-at     (-> item :modified-at)
        user-first-name (-> item :modified-by :user-profile/first-name)
        user-last-name  (-> item :modified-by :user-profile/last-name)
        user-full-name @(r/subscribe [:x.locales/get-ordered-name user-first-name user-last-name])
        timestamp      @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        modified        (string/join [user-full-name timestamp] ", " {:join-empty? false})
        modified        {:content :last-modified-n :replacements [modified]}]
       [surface-description.views/component info-id
                                            {:content  modified
                                             :disabled? disabled?}]))

(defn component
  ; @param (keyword)(opt) info-id
  ; @param (map) info-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-path (vector)}
  ;
  ; @usage
  ;  [item-info {...}]
  ;
  ; @usage
  ;  [item-info :my-item-info {...}]
  ([info-props]
   [component (random/generate-keyword) info-props])

  ([info-id info-props]
   [item-info info-id info-props]))
