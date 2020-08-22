package common.util.anim

import common.CommonStatic
import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import common.io.assets.AssetLoader
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.json.JsonEncoder
import common.io.json.Test
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.Test.JsonTest_2
import common.pack.Source.AnimLoader
import common.pack.Source.ResourceLocation
import common.pack.Source.SourceAnimLoader
import common.pack.Source.SourceAnimSaver
import common.pack.Source.Workspace
import common.pack.Source.ZipSource
import common.system.VImg
import common.system.fake.FakeImage
import common.system.fake.FakeImage.Marker
import common.util.anim.AnimCI.AnimCIKeeper
import common.util.stage.EStage
import common.util.stage.StageMap
import common.util.stage.StageMap.StageMapInfo
import common.util.unit.UnitLevel
import io.BCPlayer
import page.JL
import page.anim.AnimBox
import page.support.ListJtfPolicy
import page.support.SortTable
import page.view.ViewBox
import page.view.ViewBox.Conf
import page.view.ViewBox.Controller
import page.view.ViewBox.VBExporter

open class AnimCI(acl: AnimLoader) : AnimU<AnimCIKeeper?>(AnimCIKeeper(acl)) {
    protected class AnimCIKeeper(al: AnimLoader) : ImageKeeper {
        val loader: AnimLoader
        private var num: FakeImage? = null
        private var ediLoaded = false
        private var edi: VImg? = null
        private var uni: VImg? = null
        override fun getEdi(): VImg? {
            if (ediLoaded) return edi
            ediLoaded = true
            edi = loader.getEdi()
            if (edi != null) edi.mark(Marker.EDI)
            return edi
        }

        override fun getIC(): ImgCut {
            return loader.getIC()
        }

        override fun getMA(): Array<MaAnim?> {
            return loader.getMA()
        }

        override fun getMM(): MaModel {
            return loader.getMM()
        }

        fun getName(): ResourceLocation {
            return loader.getName()
        }

        override fun getNum(): FakeImage? {
            return if (num != null && num.bimg() != null && num.isValid()) num else loader.getNum().also { num = it }
        }

        fun getStatus(): Int {
            return loader.getStatus()
        }

        override fun getUni(): VImg? {
            if (uni != null) return uni
            uni = loader.getUni()
            if (uni != null) uni.mark(Marker.UNI) else uni = CommonStatic.getBCAssets().slot.get(0)
            return uni
        }

        fun setEdi(vedi: VImg?) {
            edi = vedi
            if (vedi != null) vedi.mark(Marker.EDI)
            ediLoaded = true
        }

        fun setNum(fimg: FakeImage?) {
            num = fimg
        }

        fun setUni(vuni: VImg?) {
            uni = vuni
            uni.mark(Marker.UNI)
        }

        override fun unload() {}

        init {
            loader = al
        }
    }

    var id: ResourceLocation
    override fun load() {
        try {
            super.load()
            if (getEdi() != null) getEdi().check()
            if (getUni() != null) getUni().check()
        } catch (e: Exception) {
            e.printStackTrace()
            CommonStatic.def.exit(false)
        }
        validate()
    }

    override fun toString(): String {
        return id.id
    }

    init {
        id = loader.getName()
    }
}