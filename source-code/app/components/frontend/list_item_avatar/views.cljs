
(ns app.components.frontend.list-item-avatar.views
    (:require [app.components.frontend.user-avatar.views :as user-avatar.views]
              [random.api                                :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-avatar
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  [avatar-id avatar-props]
  ; BUG#0781 (source-code/app/components/frontend/item_list_table/views.cljs)
  [:td {:style {:width "78px" :height "72px"}}
       [user-avatar.views/component avatar-id (assoc avatar-props :indent {:vertical :s})]])

(defn component
  ; @param (keyword)(opt) avatar-id
  ; @param (map) avatar-props
  ;  {:colors (strings in vector)(opt)
  ;   :first-name (string)(opt)
  ;   :last-name (string)(opt)
  ;   :size (px)(opt)}
  ;
  ; @usage
  ;  [list-item-avatar {...}]
  ;
  ; @usage
  ;  [list-item-avatar :my-avatar {...}]
  ([avatar-props]
   [component (random/generate-keyword) avatar-props])

  ([avatar-id avatar-props]
   [list-item-avatar avatar-id avatar-props]))
