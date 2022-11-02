
(ns app.models.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.app-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group      :vehicles
                                                            :icon       :view_agenda
                                                            :icon-color "#4a79bf"
                                                            :label      :models
                                                            :on-click   [:router/go-to! "/@app-home/models"]
                                                            :horizontal-weight 1}]
                              [:home.sidebar/add-menu-item! {:group      :vehicles
                                                             :icon       :view_agenda
                                                             :icon-color "#4a79bf"
                                                             :label      :models
                                                             :on-click   [:router/go-to! "/@app-home/models"]
                                                             :vertical-weight 1}]]}})
